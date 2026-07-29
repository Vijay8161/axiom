package com.pm.axiom.dto.section;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateSectionRequest(

        @NotBlank(message = "Section title is required")
        @Size(max = 200, message = "Section title must be at most 200 characters")
        String title,

        @NotNull(message = "Display order is required")
        @PositiveOrZero(message = "Display order must be zero or greater")
        Integer displayOrder
) {}