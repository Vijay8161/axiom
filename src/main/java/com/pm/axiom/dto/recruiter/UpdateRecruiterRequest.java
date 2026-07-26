package com.pm.axiom.dto.recruiter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRecruiterRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must be at most 150 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name must be at most 150 characters")
        String companyName
) {}
