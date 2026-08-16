package com.pm.axiom.dto.section;

import com.pm.axiom.dto.question.CreateQuestionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSectionRequest(

        @NotBlank(message = "Section title is required")
        @Size(max = 200, message = "Section title must be at most 200 characters")
        String title,

        @NotNull(message = "Display order is required")
        @PositiveOrZero(message = "Display order must be zero or greater")
        Integer displayOrder
) {}