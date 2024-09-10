package com.gold.auth.user.dto;

import com.gold.auth.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponse {
    @Schema(description = "아이디", example = "aletheia_user")
    private String username;

    @Schema(description = "사용자/관리자", example = "사용자")
    private String role;

    @Schema(description = "가입 일자", example = "2024-09-09")
    private LocalDate registeredDate;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.role = user.getRole().equals("USER") ? "사용자" : "관리자";
        this.registeredDate = user.getCreatedDate().toLocalDate();
    }
}
