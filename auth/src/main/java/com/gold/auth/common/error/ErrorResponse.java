package com.gold.auth.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예외처리 응답 객체
 */
@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String message;
}
