package com.pm.axiom.dto.question;

import com.pm.axiom.entity.CorrectOption;

import java.time.Instant;

public record QuestionResponse(
        Long id,
        Long assessmentId,
        String question,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        CorrectOption correctOption,
        Integer marks,
        Instant createdAt
) {}