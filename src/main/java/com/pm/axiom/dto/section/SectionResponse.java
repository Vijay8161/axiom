package com.pm.axiom.dto.section;

import com.pm.axiom.dto.question.QuestionResponse;

import java.util.List;

public record SectionResponse(
        Long id,
        String title,
        Integer displayOrder,
        List<QuestionResponse> questions
) {}