package com.pm.axiom.dto.recruiter;

import com.pm.axiom.entity.Role;

import java.time.Instant;

public record RecruiterResponse(
        Long id,
        String name,
        String email,
        String companyName,
        Role role,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {}