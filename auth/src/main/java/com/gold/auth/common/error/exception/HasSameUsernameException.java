package com.gold.auth.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 아이디 중복 시
 */
@Getter
@AllArgsConstructor
public class HasSameUsernameException extends RuntimeException {
    private String message;
}
