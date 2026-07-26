package com.pm.axiom.service.question;

import com.pm.axiom.dto.question.CreateQuestionRequest;
import com.pm.axiom.dto.question.QuestionResponse;
import com.pm.axiom.dto.question.UpdateQuestionRequest;
import com.pm.axiom.entity.Assessment;
import com.pm.axiom.entity.MCQQuestion;
import com.pm.axiom.exception.ResourceNotFoundException;
import com.pm.axiom.mapper.QuestionMapper;
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
public class QuestionService {

    private final MCQQuestionRepository mcqQuestionRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuestionMapper questionMapper;

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public QuestionResponse createQuestion(Long assessmentId, CreateQuestionRequest request) {
        Assessment assessment = getOwnedAssessmentOrThrow(assessmentId);

        MCQQuestion question = MCQQuestion.builder()
                .assessment(assessment)
                .question(request.question())
                .optionA(request.optionA())
                .optionB(request.optionB())
                .optionC(request.optionC())
                .optionD(request.optionD())
                .correctOption(request.correctOption())
                .marks(request.marks())
                .build();

        return questionMapper.toResponse(mcqQuestionRepository.save(question));
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsForAssessment(Long assessmentId) {
        Assessment assessment = getViewableAssessmentOrThrow(assessmentId);

        return mcqQuestionRepository.findAllByAssessmentIdOrderByIdAsc(assessment.getId())
                .stream()
                .map(questionMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public QuestionResponse updateQuestion(Long id, UpdateQuestionRequest request) {
        MCQQuestion question = getOwnedQuestionOrThrow(id);

        question.setQuestion(request.question());
        question.setOptionA(request.optionA());
        question.setOptionB(request.optionB());
        question.setOptionC(request.optionC());
        question.setOptionD(request.optionD());
        question.setCorrectOption(request.correctOption());
        question.setMarks(request.marks());

        return questionMapper.toResponse(question);
    }

    @PreAuthorize("hasRole('ADMIN_RECRUITER')")
    @Transactional
    public void deleteQuestion(Long id) {
        mcqQuestionRepository.delete(getOwnedQuestionOrThrow(id));
    }

    private Assessment getViewableAssessmentOrThrow(Long assessmentId) {
        Assessment assessment = assessmentRepository.findByIdWithCreatedBy(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        boolean isOwner = assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId());
        if (!SecurityUtils.isAdmin() && !isOwner) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessmentId);
        }
        return assessment;
    }

    private Assessment getOwnedAssessmentOrThrow(Long assessmentId) {
        Assessment assessment = assessmentRepository.findByIdWithCreatedBy(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (!assessment.getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessmentId);
        }
        return assessment;
    }

    private MCQQuestion getOwnedQuestionOrThrow(Long id) {
        MCQQuestion question = mcqQuestionRepository.findByIdWithAssessmentAndCreatedBy(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        if (!question.getAssessment().getCreatedBy().getId().equals(SecurityUtils.getCurrentRecruiterId())) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        return question;
    }
}
