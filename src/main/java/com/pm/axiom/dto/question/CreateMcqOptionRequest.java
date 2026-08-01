package com.pm.axiom.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateMcqOptionRequest(

        @NotBlank(message = "Option text is required")
        @Size(max = 500, message = "Option text must be at most 500 characters")
        String text,

        @NotNull(message = "Display order is required")
        @PositiveOrZero(message = "Display order must be zero or greater")
        Integer displayOrder,

        boolean correct
) {}