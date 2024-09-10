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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * 인증 토큰 생성, 검증 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {
    // Refresh token을 Redis에 저장하기 위한 리파지토리
    private final ActiveUserRepository activeUserRepository;

    // JWT 비밀키
    @Value("${jwt.secret}")
    private String secret;

    // JWT 서명을 위한 키 객체
    private Key key;

    // 클래스 초기화 시 비밀키를 이용해 Key 객체 생성
    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    /**
     * JWT 토큰을 AccessToken 객체로 변환
     * @param token token JWT 토큰 문자열
     * @return AccessToken 객체
     */
    public AccessToken convertAccessToken(String token) {
        return new AccessToken(token, key);
    }

    /**
     * 인증 정보를 SecurityContext에 저장
     *
     * @param userId 사용자 ID
     * @param username 사용자 이름
     * @param role 사용자 역할
     */
    public void setAuthentication(Long userId, String username, String role) {
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
        AletheiaUser user = new AletheiaUser(userId, username, authorities);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    /**
     * 사용자 정보를 바탕으로 AccessToken 생성
     *
     * @param user 사용자 객체
     * @return 생성된 AccessToken
     */
    public AccessToken generateAccessToken(User user) {
        return generateAccessToken(user.getId(), user.getUsername(), user.getRole());
    }

    /**
     * 사용자 정보를 바탕으로 RefreshToken 생성, Redis에 저장
     *
     * @param userId 사용자 ID
     * @param username 사용자 이름
     * @return 생성된 RefreshToken
     */
    public RefreshToken generateRefreshToken(Long userId, String username) {
        RefreshToken refreshToken = new RefreshToken();

        // Refresh token Redis에 저장
        ActiveUser activeUser = new ActiveUser(userId, username, refreshToken);
        activeUserRepository.save(activeUser);

        return refreshToken;
    }

    /**
     * 주어진 RefreshToken을 바탕으로 새로운 AccessToken 생성
     *
     * @param refreshToken 검증할 RefreshToken
     * @return 생성된 AccessToken
     * @throws NoSuchElementException RefreshToken이 유효하지 않을 경우
     * @throws RefreshTokenException RefreshToken이 만료된 경우
     */
    public AccessToken refreshAccessToken(String refreshToken) {
        ActiveUser activeUser = activeUserRepository.findById(refreshToken)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.INVALID_REFRESH_TOKEN.getMessage()));

        if (activeUser.getExpiredAt().isBefore(LocalDateTime.now())) {
            // refresh token 만료
            activeUserRepository.deleteById(refreshToken); // Redis에서 삭제
            throw new RefreshTokenException(ErrorMessage.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        return generateAccessToken(activeUser);
    }

    /**
     * 주어진 RefreshToken을 Redis에서 삭제
     *
     * @param refreshToken 삭제할 RefreshToken
     */
    public void deleteRefreshToken(String refreshToken) {
        activeUserRepository.deleteById(refreshToken);
    }

    /**
     * 사용자 정보를 바탕으로 AccessToken 생성
     *
     * @param userId 사용자 ID
     * @param username 사용자 이름
     * @param role 사용자 역할
     * @return 생성된 AccessToken
     */
    private AccessToken generateAccessToken(Long userId, String username, String role) {
        return new AccessToken(userId, username, role, key);
    }

    /**
     * ActiveUser 객체(Redis 엔티티)를 바탕으로 AccessToken 생성
     *
     * @param user ActiveUser 객체
     * @return 생성된 AccessToken
     */
    private AccessToken generateAccessToken(ActiveUser user) {
        return generateAccessToken(user.getId(), user.getUsername(), user.getRole());
    }
}
