package com.gold.auth.user.dto;

import com.gold.auth.common.validation.IsRole;
import com.gold.auth.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterRequest {
    @Schema(description = "아이디", example = "aletheia_user")
    @NotBlank(message = "아이디는 비워둘 수 없습니다.")
    @Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하여야 합니다.")
    private String username;

    @Schema(description = "비밀번호", example = "password1234!")
    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=()]).*$",
            message = "비밀번호는 숫자, 문자, 특수문자(!@#$%^&*+=())를 모두 포함해야합니다.")
    private String password;

    @Schema(description = "권한", example = "USER")
    @IsRole
    private String role;

    public void encryptPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public User toEntity() {
        return new User(username, password, role);
    }
}
