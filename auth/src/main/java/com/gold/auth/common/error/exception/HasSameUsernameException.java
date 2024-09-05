package com.gold.auth.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HasSameUsernameException extends RuntimeException {
    private String message;
}
