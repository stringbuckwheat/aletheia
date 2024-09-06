package com.gold.auth.user.service;

import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.LoginRequest;
import com.gold.auth.user.dto.RegisterRequest;

public interface UserService {
    String hasSameUsername(String username);

    AuthTokens register(RegisterRequest register);

    // 로그인
    AuthTokens login(LoginRequest loginRequest);

    String reissueToken(String refreshToken);

    void logOut(String refreshToken);
}
