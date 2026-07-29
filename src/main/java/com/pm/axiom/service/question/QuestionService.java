package com.pm.axiom.service.question;

import com.pm.axiom.dto.question.CreateQuestionRequest;
import com.pm.axiom.dto.question.QuestionResponse;
import com.pm.axiom.dto.question.UpdateCodingQuestionRequest;
import com.pm.axiom.dto.question.UpdateDescriptiveQuestionRequest;
import com.pm.axiom.dto.question.UpdateMCQQuestionRequest;
import com.pm.axiom.dto.question.UpdateQuestionRequest;
import com.pm.axiom.entity.CodingQuestion;
import com.pm.axiom.entity.DescriptiveQuestion;
import com.pm.axiom.entity.MCQQuestion;
import com.pm.axiom.entity.Question;
import com.pm.axiom.entity.Section;
import com.pm.axiom.exception.BusinessRuleViolationException;
import com.pm.axiom.exception.ResourceNotFoundException;
import com.pm.axiom.mapper.QuestionMapper;
import com.pm.axiom.repository.QuestionRepository;
import com.pm.axiom.repository.SectionRepository;
import com.pm.axiom.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SectionRepository sectionRepository;
    private final QuestionMapper questionMapper;

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public QuestionResponse createQuestion(Long sectionId, CreateQuestionRequest request) {
        Section section = getOwnedSectionOrThrow(sectionId);

        Question question = questionMapper.toEntity(request);
        question.setSection(section);

        return questionMapper.toResponse(questionRepository.save(question));
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public QuestionResponse updateQuestion(Long id, UpdateQuestionRequest request) {
        Question question = getOwnedQuestionOrThrow(id);
        assertTypeMatches(question, request);

        questionMapper.updateEntity(request, question);
        return questionMapper.toResponse(question);
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public void deleteQuestion(Long id) {
        Question question = getOwnedQuestionOrThrow(id);

        if (question.getSection().getAssessment().isPublished()) {
            throw new BusinessRuleViolationException(
                    "Questions cannot be removed from a published assessment — unpublish it first");
        }

        if (questionRepository.countBySectionId(question.getSection().getId()) <= 1) {
            throw new BusinessRuleViolationException("A section must contain at least one question");
        }

        questionRepository.delete(question);
    }

    /**
     * A question's concrete type is fixed at creation — switching MCQ to Coding (etc.) on update
     * would mean silently discarding type-specific data (e.g. every MCQ option). Exhaustive switch
     * over the sealed UpdateQuestionRequest, no default: the compiler forces this to be revisited
     * whenever a new question type is added.
     */
    private void assertTypeMatches(Question existing, UpdateQuestionRequest request) {
        boolean matches = switch (request) {
            case UpdateMCQQuestionRequest r -> existing instanceof MCQQuestion;
            case UpdateCodingQuestionRequest r -> existing instanceof CodingQuestion;
            case UpdateDescriptiveQuestionRequest r -> existing instanceof DescriptiveQuestion;
        };

        if (!matches) {
            throw new BusinessRuleViolationException(
                    "Cannot change question " + existing.getId() + " to type " + request.type()
                            + " — delete and recreate it instead");
        }
    }

    private Section getOwnedSectionOrThrow(Long sectionId) {
        Section section = sectionRepository.findByIdWithAssessmentAndCreatedBy(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + sectionId));

        if (!section.getAssessment().getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Section not found with id: " + sectionId);
        }
        return section;
    }

    private Question getOwnedQuestionOrThrow(Long id) {
        Question question = questionRepository.findByIdWithSectionAndCreatedBy(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        Long ownerId = question.getSection().getAssessment().getCreatedBy().getId();
        if (!ownerId.equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        return question;
    }
}