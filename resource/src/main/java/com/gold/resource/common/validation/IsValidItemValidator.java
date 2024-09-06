package com.gold.resource.common.validation;

import com.gold.resource.sales.enums.Item;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsValidItemValidator implements ConstraintValidator<IsValidItem, String> {

    @Override
    public void initialize(IsValidItem annotation) {
        ConstraintValidator.super.initialize(annotation);
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        return Item.fromValue(value) != null;
    }
}

