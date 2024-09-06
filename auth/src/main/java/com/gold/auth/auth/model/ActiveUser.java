package com.gold.auth.auth.model;

import com.gold.auth.auth.token.RefreshToken;
import com.gold.auth.common.validation.IsRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/**
 * Redis 저장용 엔티티
 */
@Getter
@RedisHash(value = "activeUser", timeToLive = 14400) // 4시간 저장
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActiveUser {
    @Id
    private String refreshToken;

    private LocalDateTime expiredAt; // 만료 시각

    @NotNull
    private Long id; // user pk

    @NotBlank
    private String username;

    @IsRole
    @NotBlank
    private String role;
    private LocalDateTime createdAt;

    public ActiveUser(Long id, String username, RefreshToken refreshToken) {
        this.id = id;
        this.username = username;

        this.refreshToken = refreshToken.getToken();
        this.expiredAt = refreshToken.getExpiredAt();

        this.createdAt = LocalDateTime.now();
    }
}
