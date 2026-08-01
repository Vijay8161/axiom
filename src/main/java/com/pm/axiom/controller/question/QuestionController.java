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

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/api/sections/{sectionId}/questions")
    public ResponseEntity<QuestionResponse> createQuestion(
            @PathVariable Long sectionId,
            @Valid @RequestBody CreateQuestionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(sectionId, request));
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