package com.gold.auth.common.docs;

import com.gold.auth.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 공통 Swagger 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized: 인증되지 않은 사용자",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class),
                                examples = @ExampleObject(
                                        value = "{\"message\": \"유효하지 않은 액세스 토큰입니다. 다시 로그인해주세요\"}"
                                )
                        )
                ),
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
        }
)
public @interface ApiDocs {
    String summary() default "";
    String description() default "";
}

