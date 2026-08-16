package com.pm.axiom.service.assessment;

import com.pm.axiom.dto.assessment.AssessmentResponse;
import com.pm.axiom.dto.assessment.AssessmentSummaryResponse;
import com.pm.axiom.dto.assessment.CreateAssessmentRequest;
import com.pm.axiom.dto.assessment.UpdateAssessmentRequest;
import com.pm.axiom.entity.Assessment;
import com.pm.axiom.entity.Question;
import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Section;
import com.pm.axiom.exception.BusinessRuleViolationException;
import com.pm.axiom.exception.ResourceNotFoundException;
import com.pm.axiom.mapper.AssessmentMapper;
import com.pm.axiom.repository.AssessmentRepository;
import com.pm.axiom.repository.QuestionRepository;
import com.pm.axiom.repository.SectionRepository;
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
    private final SectionRepository sectionRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentMapper assessmentMapper;

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse createAssessment(CreateAssessmentRequest request) {
        Assessment assessment = assessmentMapper.toEntity(request);
        assessment.setPublished(false);
        stampCreatedBy(assessment, SecurityUtils.getCurrentRecruiter());

        Assessment saved = assessmentRepository.save(assessment);
        return assessmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> getAllAssessments() {
        List<Assessment> assessments = SecurityUtils.isAdmin()
                ? assessmentRepository.findAllWithCreatedBy()
                : assessmentRepository.findAllByCreatedBy(SecurityUtils.getCurrentRecruiter());

        return assessments.stream()
                .map(a -> assessmentMapper.toSummaryResponse(
                        a,
                        (int) sectionRepository.countByAssessmentId(a.getId()),
                        questionRepository.countBySectionAssessmentId(a.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public AssessmentResponse getAssessmentById(Long id) {
        Assessment assessment = findWithSectionsOrThrow(id);
        assertViewable(assessment);
        return assessmentMapper.toResponse(assessment);
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse updateAssessment(Long id, UpdateAssessmentRequest request) {
        Assessment assessment = getOwnedAssessmentWithSectionsOrThrow(id);
        assessmentMapper.updateEntity(request, assessment);
        return assessmentMapper.toResponse(assessment);
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
        Assessment assessment = getOwnedAssessmentWithSectionsOrThrow(id);
        assessment.setPublished(true);
        return assessmentMapper.toResponse(assessment);
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public AssessmentResponse unpublishAssessment(Long id) {
        Assessment assessment = getOwnedAssessmentWithSectionsOrThrow(id);
        assessment.setPublished(false);
        return assessmentMapper.toResponse(assessment);
    }

    /** The whole nested tree (assessment + sections + questions) is created by one recruiter
     in one call — stamp all of it, not just the root, so audit data is complete. */
    private void stampCreatedBy(Assessment assessment, Recruiter recruiter) {
        assessment.setCreatedBy(recruiter);
        for (Section section : assessment.getSections()) {
            section.setCreatedBy(recruiter);
            for (Question question : section.getQuestions()) {
                question.setCreatedBy(recruiter);
            }
        }
    }

    private Assessment findWithSectionsOrThrow(Long id) {
        return assessmentRepository.findByIdWithSections(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    private Assessment findByIdOrThrow(Long id) {
        return assessmentRepository.findByIdWithCreatedBy(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    private void assertViewable(Assessment assessment) {
        boolean isOwner = assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId());
        if (!SecurityUtils.isAdminRecruiter() && !isOwner) {
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

    private Assessment getOwnedAssessmentWithSectionsOrThrow(Long id) {
        Assessment assessment = findWithSectionsOrThrow(id);
        if (!assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Assessment not found with id: " + id);
        }
        return assessment;
    }
}