package com.pm.axiom.dto.question;

import com.pm.axiom.entity.Difficulty;

public record CodingQuestionResponse(
        Long id,
        QuestionType type,
        String text,
        Integer marks,
        Integer displayOrder,
        Difficulty difficulty,
        String explanation,
        String starterCode,
        String language
) implements QuestionResponse {}