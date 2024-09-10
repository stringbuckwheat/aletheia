package com.gold.auth.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RefreshToken 관련 오류
 * 1) RefreshToken이 도착하지 않았을 때
 * 2) RefreshToken 만료
 * 3) Redis에 RefreshToken이 존재하지 않을 때
 */
@Getter
@AllArgsConstructor
public class RefreshTokenException extends RuntimeException {
    private String message;
}
