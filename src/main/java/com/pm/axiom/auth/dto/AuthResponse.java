package com.pm.axiom.auth.dto;

import com.pm.axiom.entity.Role;

public record AuthResponse(
        String token,
        String tokenType,
        Long recruiterId,
        String name,
        String email,
        Role role
) {
    public static AuthResponse of(String token, Long recruiterId, String name, String email, Role role) {
        return new AuthResponse(token, "Bearer", recruiterId, name, email, role);
    }
}
