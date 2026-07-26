package com.pm.axiom.service.assessment;

import com.pm.axiom.dto.assessment.AssessmentResponse;
import com.pm.axiom.dto.assessment.CreateAssessmentRequest;
import com.pm.axiom.dto.assessment.UpdateAssessmentRequest;
import com.pm.axiom.entity.Assessment;
import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.exception.BusinessRuleViolationException;
import com.pm.axiom.exception.ResourceNotFoundException;
import com.pm.axiom.mapper.AssessmentMapper;
import com.pm.axiom.repository.AssessmentRepository;
import com.pm.axiom.repository.MCQQuestionRepository;
import com.pm.axiom.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final MCQQuestionRepository mcqQuestionRepository;
    private final AssessmentMapper assessmentMapper;

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse createAssessment(CreateAssessmentRequest request) {
        Recruiter currentRecruiter = SecurityUtils.getCurrentRecruiter();

        Assessment assessment = Assessment.builder()
                .title(request.title())
                .description(request.description())
                .durationMinutes(request.durationMinutes())
                .published(false)
                .createdBy(currentRecruiter)
                .build();

        Assessment saved = assessmentRepository.save(assessment);
        return assessmentMapper.toResponse(saved, 0L); // brand new — no questions yet
    }

    @Transactional(readOnly = true)
    public List<AssessmentResponse> getAllAssessments() {
        List<Assessment> assessments = SecurityUtils.isAdmin()
                ? assessmentRepository.findAllWithCreatedBy()
                : assessmentRepository.findAllByCreatedBy(SecurityUtils.getCurrentRecruiter());

        return assessments.stream()
                .map(a -> assessmentMapper.toResponse(a, mcqQuestionRepository.countByAssessmentId(a.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public AssessmentResponse getAssessmentById(Long id) {
        Assessment assessment = findByIdOrThrow(id);
        assertViewable(assessment);
        return assessmentMapper.toResponse(assessment, mcqQuestionRepository.countByAssessmentId(id));
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse updateAssessment(Long id, UpdateAssessmentRequest request) {
        Assessment assessment = getOwnedAssessmentOrThrow(id);

        assessment.setTitle(request.title());
        assessment.setDescription(request.description());
        assessment.setDurationMinutes(request.durationMinutes());

        return assessmentMapper.toResponse(assessment, mcqQuestionRepository.countByAssessmentId(id));
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public void deleteAssessment(Long id) {
        Assessment assessment = getOwnedAssessmentOrThrow(id);

        if (assessment.isPublished()) {
            throw new BusinessRuleViolationException(
                    "A published assessment must be unpublished before it can be deleted");
        }

        assessmentRepository.delete(assessment);
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse publishAssessment(Long id) {
        Assessment assessment = getOwnedAssessmentOrThrow(id);
        assessment.setPublished(true);
        return assessmentMapper.toResponse(assessment, mcqQuestionRepository.countByAssessmentId(id));
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse unpublishAssessment(Long id) {
        Assessment assessment = getOwnedAssessmentOrThrow(id);
        assessment.setPublished(false);
        return assessmentMapper.toResponse(assessment, mcqQuestionRepository.countByAssessmentId(id));
    }

    private Assessment findByIdOrThrow(Long id) {
        return assessmentRepository.findByIdWithCreatedBy(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    private void assertViewable(Assessment assessment) {
        boolean isOwner = assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId());
        if (!SecurityUtils.isAdmin() && !isOwner) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessment.getId());
        }
    }

    private Assessment getOwnedAssessmentOrThrow(Long id) {
        Assessment assessment = findByIdOrThrow(id);

        if (!assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Assessment not found with id: " + id);
        }

        return assessment;
    }
}