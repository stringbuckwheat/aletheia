package com.gold.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
}
