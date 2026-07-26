package com.pm.axiom.controller.recruiter;

import com.pm.axiom.dto.recruiter.RecruiterResponse;
import com.pm.axiom.dto.recruiter.UpdateRecruiterRequest;
import com.pm.axiom.service.recruiter.RecruiterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @GetMapping
    public ResponseEntity<List<RecruiterResponse>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruiterResponse> getRecruiterById(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruiterResponse> updateRecruiter(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRecruiterRequest request
    ) {
        return ResponseEntity.ok(recruiterService.updateRecruiter(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<RecruiterResponse> enableRecruiter(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.enableRecruiter(id));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<RecruiterResponse> disableRecruiter(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.disableRecruiter(id));
    }
}
