package com.gold.resource.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsValidSalesStateValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidSalesState {
    String message() default  "유효하지 않은 상태 요청입니다. 'GOLD_999' 또는 'GOLD_9999' 중 하나를 선택해 주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}