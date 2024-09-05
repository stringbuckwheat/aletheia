package com.gold.auth.user.controller;

import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.LoginRequest;
import com.gold.auth.user.dto.RegisterRequest;
import com.gold.auth.user.dto.UsernameCheck;
import com.gold.auth.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/api/auth/user/username")
    public ResponseEntity<String> hasSameUsername(@RequestBody UsernameCheck usernameCheck) {
        return ResponseEntity.ok().body(userService.hasSameUsername(usernameCheck.getUsername()));
    }

    @PostMapping("/api/auth/user/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(register));
    }

    @PostMapping("/api/auth/user/login")
    public ResponseEntity<AuthTokens> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(loginRequest));
    }
}
