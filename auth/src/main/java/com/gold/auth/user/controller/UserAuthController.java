package com.gold.auth.user.controller;

import com.gold.auth.auth.security.AletheiaUser;
import com.gold.auth.common.docs.ApiDocs;
import com.gold.auth.common.error.ErrorResponse;
import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.PasswordRequest;
import com.gold.auth.user.dto.UserResponse;
import com.gold.auth.user.service.UserAuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User - Auth", description = "인증이 필요한 컨트롤러")
@RequestMapping("/api/auth/user")
public class UserAuthController {
    private final UserAuthService userAuthService;

    @ApiDocs(
            summary = "회원 정보 보기",
            description = "본인의 정보만 조회 가능"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK: 본인의 가입 정보",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND: 해당 유저 정보를 찾을 수 없음",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"해당 유저를 찾을 수 없습니다.\"}"
                    )
            )
    )
    @GetMapping("")
    public ResponseEntity<UserResponse> getDetail(@AuthenticationPrincipal AletheiaUser user) {
        return ResponseEntity.ok().body(userAuthService.getDetail(user.getId()));
    }

    @ApiDocs(
            summary = "비밀번호 수정"
    )
    @ApiResponse(
            responseCode = "204",
            description = "NO_CONTENT: 비밀번호 수정 완료"
    )
    @ApiResponse(
            responseCode = "409",
            description = "CONFLICT: 이전 비밀번호와 수정하려는 비밀번호가 동일한 경우",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"직전과 똑같은 비밀번호를 사용할 수 없습니다.\"}"
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청: 요청의 필수 입력값 누락 또는 잘못된 데이터",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"아이디는 3자 이상 20자 이하여야 합니다.\"}"
                    )
            )
    )
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid PasswordRequest passwordRequest,
                                               @AuthenticationPrincipal AletheiaUser user) {
        userAuthService.updatePassword(passwordRequest, user.getId());
        return ResponseEntity.noContent().build();
    }

    @ApiDocs(
            summary = "회원 탈퇴",
            description = """
                    사용자 삭제 전 다음 작업 수행:
                    <ul>
                        <li>RefreshToken을 사용하여 로그아웃</li>
                        <li>사용자 ID에 해당하는 (탈퇴하지 않은) 사용자 존재 여부 확인 및 예외 발생</li>
                        <li>사용자의 `deleteAt` 필드에 현재 시각을 저장하여 Soft Delete 수행</li>
                    </ul>
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "NO_CONTENT: 탈퇴 완료"
    )
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody String refreshToken, @AuthenticationPrincipal AletheiaUser user) {
        userAuthService.delete(user.getId(), refreshToken);
        return ResponseEntity.noContent().build();
    }
}
