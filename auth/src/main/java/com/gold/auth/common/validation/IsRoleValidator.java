package com.gold.auth.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsRoleValidator implements ConstraintValidator<IsRole, String> {

    @Override
    public void initialize(IsRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.equals("USER") || value.equals("ADMIN");
    }
}
