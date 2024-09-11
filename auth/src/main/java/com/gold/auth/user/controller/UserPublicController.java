package com.gold.auth.user.controller;

import com.gold.auth.common.error.ErrorResponse;
import com.gold.auth.user.dto.*;
import com.gold.auth.user.service.UserPublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User - Public", description = "인증이 필요없는 컨트롤러")
@RequestMapping("/api/auth/public/user")
public class UserPublicController {
    private final UserPublicService userPublicService;

    @Operation(
            summary = "아이디 중복 검사",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 사용 가능한 아이디",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "CONFLICT: 아이디 중복",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"aletheia_user은/는 이미 사용중인 아이디입니다.\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/username")
    public ResponseEntity<String> hasSameUsername(@RequestBody UsernameCheck usernameCheck) {
        return ResponseEntity.ok().body(userPublicService.hasSameUsername(usernameCheck.getUsername()));
    }

    @Operation(
            summary = "회원가입",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED: 회원 가입 성공, Access/Refresh Token 반환",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthTokens.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "CONFLICT: 아이디 중복",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"aletheia_user은/는 이미 사용중인 아이디입니다.\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청: 요청의 필수 입력값 누락 또는 잘못된 데이터",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"품목은 필수입니다.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthTokens> register(@RequestBody @Valid RegisterRequest register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userPublicService.register(register));
    }

    @Operation(
            summary = "로그인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 로그인 성공, Access/Refresh Token 반환",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthTokens.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: 유저 정보 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"아이디를 확인해주세요\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: 비밀번호 불일치",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"비밀번호가 일치하지 않습니다\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok().body(userPublicService.login(loginRequest));
    }

    @Operation(
            summary = "액세스 토큰 재발급",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: 새로운 Access Token 반환",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: (Redis에) 유저 정보 없음",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"유효하지 않은 리프레쉬 토큰입니다. 다시 로그인해주세요.\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: Refresh Token 만료",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"리프레쉬 토큰이 만료되었습니다. 다시 로그인해주세요.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/token")
    public ResponseEntity<String> reissueToken(@RequestBody String refreshToken){
        return ResponseEntity.ok().body(userPublicService.reissueToken(refreshToken));
    }

    @Operation(
            summary = "로그아웃",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "NO_CONTENT: 로그아웃 성공"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED: Refresh Token이 유효하지 않은 경우",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"리프레쉬 토큰이 존재하지 않습니다.\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody String refreshToken) {
        // Redis에서 REFRESH_TOKEN 삭제
        userPublicService.logOut(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
