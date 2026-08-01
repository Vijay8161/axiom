package com.pm.axiom.controller.section;

import com.pm.axiom.dto.section.CreateSectionRequest;
import com.pm.axiom.dto.section.SectionResponse;
import com.pm.axiom.dto.section.UpdateSectionRequest;
import com.pm.axiom.service.section.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/api/assessments/{assessmentId}/sections")
    public ResponseEntity<SectionResponse> createSection(
            @PathVariable Long assessmentId,
            @Valid @RequestBody CreateSectionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionService.createSection(assessmentId, request));
    }

    @PutMapping("/api/sections/{id}")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSectionRequest request
    ) {
        return ResponseEntity.ok(sectionService.updateSection(id, request));
    }

    @DeleteMapping("/api/sections/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}