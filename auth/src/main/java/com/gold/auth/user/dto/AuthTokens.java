package com.gold.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
}
