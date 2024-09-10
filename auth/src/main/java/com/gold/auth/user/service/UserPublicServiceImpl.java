package com.gold.auth.user.service;

import com.gold.auth.auth.service.TokenProvider;
import com.gold.auth.auth.token.RefreshToken;
import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.common.error.exception.RefreshTokenException;
import com.gold.auth.user.dto.*;
import com.gold.auth.common.error.exception.HasSameUsernameException;
import com.gold.auth.auth.token.AccessToken;
import com.gold.auth.user.model.User;
import com.gold.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPublicServiceImpl implements UserPublicService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    // 아이디 중복 확인
    @Override
    @Transactional(readOnly = true)
    public String hasSameUsername(String username) {
        // 아이디로 조회
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            throw new HasSameUsernameException(username + "은/는 이미 사용중인 아이디입니다.");
        }

        return username;
    }

    // 회원 가입
    @Override
    @Transactional
    public AuthTokens register(RegisterRequest register) {
        // 아이디 중복 검사
        hasSameUsername(register.getUsername());

        // 비밀번호 암호화
        register.encryptPassword(encoder.encode(register.getPassword()));

        User user = userRepository.save(register.toEntity());

        // 바로 액세스/리프레쉬 토큰 반환
        return getAuthTokens(user);
    }

    // 로그인
    @Override
    public AuthTokens login(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameAndDeletedAtIsNull(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.WRONG_USERNAME.getMessage()));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(ErrorMessage.WRONG_PASSWORD.getMessage());
        }

        return getAuthTokens(user);
    }

    // 액세스 토큰 재발급
    @Override
    @Transactional(readOnly = true)
    public String reissueToken(String refreshToken) {
        return tokenProvider.refreshAccessToken(refreshToken).getToken();
    }

    @Override
    @Transactional
    public void logOut(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new RefreshTokenException(ErrorMessage.NO_REFRESH_TOKEN.getMessage());
        }

        tokenProvider.deleteRefreshToken(refreshToken);
    }

    /**
     * Access / Refresh Token 생성
     *
     * @param user 사용자 정보
     * @return 인증 토큰
     */
    private AuthTokens getAuthTokens(User user) {
        tokenProvider.setAuthentication(user.getId(), user.getUsername(), user.getRole());

        AccessToken accessToken = tokenProvider.generateAccessToken(user);
        RefreshToken refreshToken = tokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return new AuthTokens(accessToken.getToken(), refreshToken.getToken());
    }
}
