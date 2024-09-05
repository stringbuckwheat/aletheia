package com.gold.auth.user.service;

import com.gold.auth.common.token.AccessToken;
import com.gold.auth.common.security.AletheiaUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;

@Service
public class TokenProvider {
//    private final ActiveUserRepository activeUserRepository;
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

    public void setAuthentication(Long userId, String username) {
        AletheiaUser user = new AletheiaUser(userId, username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public AccessToken generateAccessToken(Long userId, String username) {
        return new AccessToken(userId, username, key);
    }
}
