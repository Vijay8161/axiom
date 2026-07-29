package com.pm.axiom.dto.question;

import com.pm.axiom.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateDescriptiveQuestionRequest(

        @NotNull(message = "Question type is required")
        QuestionType type,

        @NotBlank(message = "Question text is required")
        @Size(max = 2000, message = "Question text must be at most 2000 characters")
        String text,

        @NotNull(message = "Marks is required")
        @Positive(message = "Marks must be greater than zero")
        Integer marks,

        @NotNull(message = "Display order is required")
        @PositiveOrZero(message = "Display order must be zero or greater")
        Integer displayOrder,

        Difficulty difficulty,

        @Size(max = 2000, message = "Explanation must be at most 2000 characters")
        String explanation,

        @Positive(message = "Max word count must be greater than zero")
        Integer maxWordCount
) implements UpdateQuestionRequest {}