package com.gold.auth.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 역할이 USER, ADMIN 둘 중 하나인지 검증하는 어노테이션
 */
@Constraint(validatedBy = IsRoleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsRole {
    String message() default "권한은 USER, ADMIN 중 하나여야합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}