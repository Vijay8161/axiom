package com.pm.axiom.dto.question;

public record McqOptionResponse(
        Long id,
        String text,
        Integer displayOrder,
        boolean correct
) {}