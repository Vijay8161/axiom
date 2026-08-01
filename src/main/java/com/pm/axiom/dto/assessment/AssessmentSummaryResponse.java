package com.pm.axiom.dto.assessment;

import java.time.Instant;

/** Flat, count-only shape — used for list views, to avoid shipping every question's full text per row. */
public record AssessmentSummaryResponse(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        boolean published,
        int sectionCount,
        long questionCount,
        Long createdById,
        String createdByName,
        Instant createdAt,
        Instant updatedAt
) {}