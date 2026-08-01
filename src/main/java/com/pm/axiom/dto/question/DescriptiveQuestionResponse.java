package com.pm.axiom.dto.question;

import com.pm.axiom.entity.Difficulty;

public record DescriptiveQuestionResponse(
        Long id,
        QuestionType type,
        String text,
        Integer marks,
        Integer displayOrder,
        Difficulty difficulty,
        String explanation,
        Integer maxWordCount
) implements QuestionResponse {}