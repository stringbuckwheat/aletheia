package com.gold.auth.auth.token;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RefreshToken {
    private String token; // 토큰
    private LocalDateTime expiredAt; // 만료일시

    public RefreshToken() {
        this.token = UUID.randomUUID().toString();
        this.expiredAt = LocalDateTime.now().plusHours(3);
    }
}