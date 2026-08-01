package com.pm.axiom.dto.question;

import com.pm.axiom.entity.Difficulty;

import java.util.List;

public record MCQQuestionResponse(
        Long id,
        QuestionType type,
        String text,
        Integer marks,
        Integer displayOrder,
        Difficulty difficulty,
        String explanation,
        List<McqOptionResponse> options
) implements QuestionResponse {}