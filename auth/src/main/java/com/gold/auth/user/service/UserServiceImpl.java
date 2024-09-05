package com.gold.auth.user.service;

import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.LoginRequest;
import com.gold.auth.user.dto.RegisterRequest;
import com.gold.auth.common.error.exception.HasSameUsernameException;
import com.gold.auth.common.token.AccessToken;
import com.gold.auth.user.model.User;
import com.gold.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    public String hasSameUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()) {
            throw new HasSameUsernameException(username + "은/는 이미 사용중인 아이디입니다.");
        }

        return username;
    }

    public String register(RegisterRequest register) {
        // 아이디 중복 검사
        hasSameUsername(register.getUsername());

        // 비밀번호 암호화
        register.encryptPassword(encoder.encode(register.getPassword()));

        User user = userRepository.save(register.toEntity());

        // TODO 바로 token들 리턴해주기
        return "회원 가입 완료";
    }

    // 로그인
    public AuthTokens login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("아이디를 확인해주세요"));

        if(!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호를 확인해주세요");
        }

        tokenProvider.setAuthentication(user.getId(), user.getUsername());

        AccessToken accessToken = tokenProvider.generateAccessToken(user.getId(), user.getUsername());
        // TODO refresh token

        return new AuthTokens(accessToken.getToken(), "refresh token");
    }
}
