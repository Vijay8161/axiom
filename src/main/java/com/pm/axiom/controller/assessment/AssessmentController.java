package com.pm.axiom.controller.assessment;

import com.pm.axiom.dto.assessment.AssessmentResponse;
import com.pm.axiom.dto.assessment.AssessmentSummaryResponse;
import com.pm.axiom.dto.assessment.CreateAssessmentRequest;
import com.pm.axiom.dto.assessment.UpdateAssessmentRequest;
import com.pm.axiom.service.assessment.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<AssessmentResponse> createAssessment(@Valid @RequestBody CreateAssessmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentService.createAssessment(request));
    }

    @GetMapping
    public ResponseEntity<List<AssessmentSummaryResponse>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponse> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssessmentResponse> updateAssessment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssessmentRequest request
    ) {
        return ResponseEntity.ok(assessmentService.updateAssessment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<AssessmentResponse> publishAssessment(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.publishAssessment(id));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<AssessmentResponse> unpublishAssessment(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.unpublishAssessment(id));
    }
}