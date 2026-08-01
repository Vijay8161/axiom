package com.pm.axiom.dto.question;

import java.util.List;

/** Shared by Create/Update MCQ requests so the correct-option validator applies to both. */
public interface HasMcqOptions {
    List<CreateMcqOptionRequest> options();
}