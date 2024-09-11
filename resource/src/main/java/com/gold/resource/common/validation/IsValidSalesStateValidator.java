package com.gold.resource.common.validation;

import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.enums.SalesStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class IsValidSalesStateValidator implements ConstraintValidator<IsValidSalesState, String> {

    @Override
    public void initialize(IsValidSalesState annotation) {
        ConstraintValidator.super.initialize(annotation);
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        SalesStatus.fromString(value);
        return true;
    }
}