package com.pm.axiom.dto.question;

import com.pm.axiom.entity.CorrectOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateQuestionRequest(

        @NotBlank(message = "Question text is required")
        @Size(max = 1000, message = "Question text must be at most 1000 characters")
        String question,

        @NotBlank(message = "Option A is required")
        @Size(max = 500, message = "Option A must be at most 500 characters")
        String optionA,

        @NotBlank(message = "Option B is required")
        @Size(max = 500, message = "Option B must be at most 500 characters")
        String optionB,

        @NotBlank(message = "Option C is required")
        @Size(max = 500, message = "Option C must be at most 500 characters")
        String optionC,

        @NotBlank(message = "Option D is required")
        @Size(max = 500, message = "Option D must be at most 500 characters")
        String optionD,

        @NotNull(message = "Correct option is required")
        CorrectOption correctOption,

        @NotNull(message = "Marks is required")
        @Positive(message = "Marks must be greater than zero")
        Integer marks
) {}