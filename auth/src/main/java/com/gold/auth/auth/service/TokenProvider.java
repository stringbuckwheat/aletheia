package com.gold.auth.auth.service;

import com.gold.auth.auth.model.ActiveUser;
import com.gold.auth.auth.repository.ActiveUserRepository;
import com.gold.auth.auth.token.AccessToken;
import com.gold.auth.auth.security.AletheiaUser;
import com.gold.auth.auth.token.RefreshToken;
import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.common.error.exception.RefreshTokenException;
import com.gold.auth.user.model.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final ActiveUserRepository activeUserRepository;
    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public AccessToken convertAccessToken(String token) {
        return new AccessToken(token, key);
    }

    public void setAuthentication(Long userId, String username, String role) {
        AletheiaUser user = new AletheiaUser(userId, username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(role)));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public AccessToken generateAccessToken(User user) {
        return generateAccessToken(user.getId(), user.getUsername(), user.getRole());
    }

    public RefreshToken generateRefreshToken(Long userId, String username) {
        RefreshToken refreshToken = new RefreshToken();

        // Refresh token Redis에 저장
        ActiveUser activeUser = new ActiveUser(userId, username, refreshToken);
        activeUserRepository.save(activeUser);

        return refreshToken;
    }

    public AccessToken refreshAccessToken(String refreshToken) {
        ActiveUser activeUser = activeUserRepository.findById(refreshToken)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND.getMessage()));

        if (activeUser.getExpiredAt().isBefore(LocalDateTime.now())) {
            // refresh token 만료
            activeUserRepository.deleteById(refreshToken); // Redis에서 삭제
            throw new RefreshTokenException(ErrorMessage.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        return generateAccessToken(activeUser);
    }

    public void deleteRefreshToken(String refreshToken) {
        activeUserRepository.deleteById(refreshToken);
    }

    private AccessToken generateAccessToken(Long userId, String username, String role) {
        return new AccessToken(userId, username, role, key);
    }

    private AccessToken generateAccessToken(ActiveUser user) {
        return generateAccessToken(user.getId(), user.getUsername(), user.getRole());
    }
}
