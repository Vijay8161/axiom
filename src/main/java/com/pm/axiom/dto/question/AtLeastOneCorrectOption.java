package com.pm.axiom.dto.question;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneCorrectOptionValidator.class)
public @interface AtLeastOneCorrectOption {
    String message() default "At least one option must be marked as correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}