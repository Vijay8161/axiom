package com.pm.axiom.controller.question;

import com.pm.axiom.dto.question.CreateQuestionRequest;
import com.pm.axiom.dto.question.QuestionResponse;
import com.pm.axiom.dto.question.UpdateQuestionRequest;
import com.pm.axiom.service.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/api/assessments/{assessmentId}/questions")
    public ResponseEntity<QuestionResponse> createQuestion(
            @PathVariable Long assessmentId,
            @Valid @RequestBody CreateQuestionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(assessmentId, request));
    }

    @GetMapping("/api/assessments/{assessmentId}/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestionsForAssessment(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(questionService.getQuestionsForAssessment(assessmentId));
    }

    @PutMapping("/api/questions/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuestionRequest request
    ) {
        return ResponseEntity.ok(questionService.updateQuestion(id, request));
    }

    @DeleteMapping("/api/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
