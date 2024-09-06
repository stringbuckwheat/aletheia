package com.gold.auth.user.controller;

import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.LoginRequest;
import com.gold.auth.user.dto.RegisterRequest;
import com.gold.auth.user.dto.UsernameCheck;
import com.gold.auth.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/api/auth/user/username")
    public ResponseEntity<String> hasSameUsername(@RequestBody UsernameCheck usernameCheck) {
        return ResponseEntity.ok().body(userService.hasSameUsername(usernameCheck.getUsername()));
    }

    @PostMapping("/api/auth/user/register")
    public ResponseEntity<AuthTokens> register(@RequestBody @Valid RegisterRequest register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(register));
    }

    @PostMapping("/api/auth/user/login")
    public ResponseEntity<AuthTokens> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok().body(userService.login(loginRequest));
    }

    @PostMapping("/api/auth/user/token/reissue")
    public ResponseEntity<String> reissueToken(@RequestBody AuthTokens authTokens){
        return ResponseEntity.ok().body(userService.reissueToken(authTokens.getRefreshToken()));
    }

    @PostMapping("/api/auth/user/logout")
    public ResponseEntity<Void> logOut(@RequestBody AuthTokens authTokens) {
        // Redis에서 REFRESH_TOKEN 삭제
        userService.logOut(authTokens.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
