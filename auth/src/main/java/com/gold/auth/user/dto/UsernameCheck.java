package com.gold.auth.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UsernameCheck {
    @Schema(description = "아이디", example = "aletheia_user")
    private String username;
}
