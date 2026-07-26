package com.pm.axiom.dto.assessment;

import java.time.Instant;

public record AssessmentResponse(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        boolean published,
        long questionCount,
        Long createdById,
        String createdByName,
        Instant createdAt,
        Instant updatedAt
) {}
