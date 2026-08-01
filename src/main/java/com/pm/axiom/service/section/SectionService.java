package com.pm.axiom.service.section;

import com.pm.axiom.dto.section.CreateSectionRequest;
import com.pm.axiom.dto.section.SectionResponse;
import com.pm.axiom.dto.section.UpdateSectionRequest;
import com.pm.axiom.entity.Assessment;
import com.pm.axiom.entity.Question;
import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Section;
import com.pm.axiom.exception.BusinessRuleViolationException;
import com.pm.axiom.exception.ResourceNotFoundException;
import com.pm.axiom.mapper.SectionMapper;
import com.pm.axiom.repository.AssessmentRepository;
import com.pm.axiom.repository.SectionRepository;
import com.pm.axiom.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final AssessmentRepository assessmentRepository;
    private final SectionMapper sectionMapper;

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public SectionResponse createSection(Long assessmentId, CreateSectionRequest request) {
        Assessment assessment = getOwnedAssessmentOrThrow(assessmentId);

        Section section = sectionMapper.toEntity(request);
        section.setAssessment(assessment);
        stampCreatedBy(section, SecurityUtils.getCurrentRecruiter());

        return sectionMapper.toResponse(sectionRepository.save(section));
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public SectionResponse updateSection(Long id, UpdateSectionRequest request) {
        Section section = getOwnedSectionOrThrow(id);
        sectionMapper.updateEntity(request, section);
        return sectionMapper.toResponse(section);
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public void deleteSection(Long id) {
        Section section = getOwnedSectionOrThrow(id);

        if (section.getAssessment().isPublished()) {
            throw new BusinessRuleViolationException(
                    "Sections cannot be removed from a published assessment — unpublish it first");
        }

        if (sectionRepository.countByAssessmentId(section.getAssessment().getId()) <= 1) {
            throw new BusinessRuleViolationException("An assessment must contain at least one section");
        }

        sectionRepository.delete(section);
    }

    /** A section created here can itself carry nested questions (see CreateSectionRequest) —
     stamp those too, same reasoning as AssessmentService.stampCreatedBy. */
    private void stampCreatedBy(Section section, Recruiter recruiter) {
        section.setCreatedBy(recruiter);
        for (Question question : section.getQuestions()) {
            question.setCreatedBy(recruiter);
        }
    }

    private Assessment getOwnedAssessmentOrThrow(Long assessmentId) {
        Assessment assessment = assessmentRepository.findByIdWithCreatedBy(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (!assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessmentId);
        }
        return assessment;
    }

    private Section getOwnedSectionOrThrow(Long id) {
        Section section = sectionRepository.findByIdWithAssessmentAndCreatedBy(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));

        if (!section.getAssessment().getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Section not found with id: " + id);
        }
        return section;
    }
}