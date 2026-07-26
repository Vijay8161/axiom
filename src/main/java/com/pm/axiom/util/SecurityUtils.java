package com.pm.axiom.util;

import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Role;
import com.pm.axiom.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Recruiter getCurrentRecruiter() {
        return getCurrentUser().getRecruiter();
    }

    public static Long getCurrentRecruiterId() {
        return getCurrentRecruiter().getId();
    }

    public static boolean isAdmin() {
        return getCurrentRecruiter().getRole() == Role.ADMIN;
    }
}
