package com.pm.axiom.dto.question;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneCorrectOptionValidator implements ConstraintValidator<AtLeastOneCorrectOption, HasMcqOptions> {

    @Override
    public boolean isValid(HasMcqOptions value, ConstraintValidatorContext context) {
        if (value == null || value.options() == null) {
            return true; // let @NotNull/@Size handle absence — this constraint only checks content
        }
        return value.options().stream().anyMatch(CreateMcqOptionRequest::correct);
    }
}