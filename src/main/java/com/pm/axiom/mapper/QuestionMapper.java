package com.pm.axiom.mapper;

import com.pm.axiom.dto.question.CodingQuestionResponse;
import com.pm.axiom.dto.question.CreateCodingQuestionRequest;
import com.pm.axiom.dto.question.CreateDescriptiveQuestionRequest;
import com.pm.axiom.dto.question.CreateMCQQuestionRequest;
import com.pm.axiom.dto.question.CreateMcqOptionRequest;
import com.pm.axiom.dto.question.CreateQuestionRequest;
import com.pm.axiom.dto.question.DescriptiveQuestionResponse;
import com.pm.axiom.dto.question.MCQQuestionResponse;
import com.pm.axiom.dto.question.McqOptionResponse;
import com.pm.axiom.dto.question.QuestionResponse;
import com.pm.axiom.dto.question.UpdateCodingQuestionRequest;
import com.pm.axiom.dto.question.UpdateDescriptiveQuestionRequest;
import com.pm.axiom.dto.question.UpdateMCQQuestionRequest;
import com.pm.axiom.dto.question.UpdateQuestionRequest;
import com.pm.axiom.entity.CodingQuestion;
import com.pm.axiom.entity.DescriptiveQuestion;
import com.pm.axiom.entity.MCQQuestion;
import com.pm.axiom.entity.McqOption;
import com.pm.axiom.entity.Question;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {

    // ---- Create: sealed CreateQuestionRequest -> abstract Question ----

    @SubclassMapping(source = CreateMCQQuestionRequest.class, target = MCQQuestion.class)
    @SubclassMapping(source = CreateCodingQuestionRequest.class, target = CodingQuestion.class)
    @SubclassMapping(source = CreateDescriptiveQuestionRequest.class, target = DescriptiveQuestion.class)
    Question toEntity(CreateQuestionRequest request);

    MCQQuestion toEntity(CreateMCQQuestionRequest request);
    CodingQuestion toEntity(CreateCodingQuestionRequest request);
    DescriptiveQuestion toEntity(CreateDescriptiveQuestionRequest request);

    McqOption toEntity(CreateMcqOptionRequest request);

    @AfterMapping
    default void wireOptionBackReferences(@MappingTarget MCQQuestion question) {
        for (McqOption option : question.getOptions()) {
            option.setQuestion(question);
        }
    }

    // ---- Update: sealed UpdateQuestionRequest -> existing Question ----
    // Precondition: question's runtime type must already match request's type —
    // enforced by QuestionService.assertTypeMatches before this is ever called.

    default void updateEntity(UpdateQuestionRequest request, @MappingTarget Question question) {
        switch (request) {
            case UpdateMCQQuestionRequest r -> updateEntity(r, (MCQQuestion) question);
            case UpdateCodingQuestionRequest r -> updateEntity(r, (CodingQuestion) question);
            case UpdateDescriptiveQuestionRequest r -> updateEntity(r, (DescriptiveQuestion) question);
        }
    }

    void updateEntity(UpdateMCQQuestionRequest request, @MappingTarget MCQQuestion question);
    void updateEntity(UpdateCodingQuestionRequest request, @MappingTarget CodingQuestion question);
    void updateEntity(UpdateDescriptiveQuestionRequest request, @MappingTarget DescriptiveQuestion question);

    // ---- Response: concrete Question subtype -> sealed QuestionResponse ----

//    @SubclassMapping(source = MCQQuestion.class, target = MCQQuestionResponse.class)
//    @SubclassMapping(source = CodingQuestion.class, target = CodingQuestionResponse.class)
//    @SubclassMapping(source = DescriptiveQuestion.class, target = DescriptiveQuestionResponse.class)
//    QuestionResponse toResponse(Question question);

    default QuestionResponse toResponse(Question question) {
        return switch (question) {
            case MCQQuestion q -> toResponse(q);
            case CodingQuestion q -> toResponse(q);
            case DescriptiveQuestion q -> toResponse(q);
            default -> null;
        };
    }

    @Mapping(target = "type", constant = "MCQ")
    MCQQuestionResponse toResponse(MCQQuestion question);

    @Mapping(target = "type", constant = "CODING")
    CodingQuestionResponse toResponse(CodingQuestion question);

    @Mapping(target = "type", constant = "DESCRIPTIVE")
    DescriptiveQuestionResponse toResponse(DescriptiveQuestion question);

    McqOptionResponse toResponse(McqOption option);
}