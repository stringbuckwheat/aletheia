package com.gold.auth.user.service;

import com.gold.auth.auth.service.TokenProvider;
import com.gold.auth.auth.token.RefreshToken;
import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.common.error.exception.RefreshTokenException;
import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.LoginRequest;
import com.gold.auth.user.dto.RegisterRequest;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional(readOnly = true)
    public String hasSameUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()) {
            throw new HasSameUsernameException(username + "은/는 이미 사용중인 아이디입니다.");
        }

        return username;
    }

    @Override
    @Transactional
    public AuthTokens register(RegisterRequest register) {
        // 아이디 중복 검사
        hasSameUsername(register.getUsername());

        // 비밀번호 암호화
        register.encryptPassword(encoder.encode(register.getPassword()));

        User user = userRepository.save(register.toEntity());

        return getAuthTokens(user);
    }

    // 로그인
    @Override
    public AuthTokens login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("아이디를 확인해주세요"));

        if(!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호를 확인해주세요");
        }

        return getAuthTokens(user);
    }

    @Override
    @Transactional(readOnly = true)
    public String reissueToken(String refreshToken) {
        return tokenProvider.refreshAccessToken(refreshToken).getToken();
    }

    @Override
    @Transactional
    public void logOut(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new RefreshTokenException(ErrorMessage.NO_REFRESH_TOKEN.getMessage());
        }

        tokenProvider.deleteRefreshToken(refreshToken);
    }

    private AuthTokens getAuthTokens(User user) {
        tokenProvider.setAuthentication(user.getId(), user.getUsername(), user.getRole());

        AccessToken accessToken = tokenProvider.generateAccessToken(user);
        RefreshToken refreshToken = tokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return new AuthTokens(accessToken.getToken(), refreshToken.getToken());
    }
}
