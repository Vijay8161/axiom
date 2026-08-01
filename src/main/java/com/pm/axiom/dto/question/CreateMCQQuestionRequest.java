package com.pm.axiom.dto.question;

import com.pm.axiom.entity.Difficulty;
import com.pm.axiom.dto.question.AtLeastOneCorrectOption;
import com.pm.axiom.dto.question.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

@AtLeastOneCorrectOption
public record CreateMCQQuestionRequest(

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

        @NotNull(message = "Options are required")
        @Size(min = 2, max = 10, message = "MCQ questions must have between 2 and 10 options")
        @Valid
        List<CreateMcqOptionRequest> options
) implements CreateQuestionRequest, HasMcqOptions {}