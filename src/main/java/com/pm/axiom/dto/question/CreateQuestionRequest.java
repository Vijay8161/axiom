package com.pm.axiom.dto.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pm.axiom.dto.question.CreateCodingQuestionRequest;
import com.pm.axiom.dto.question.CreateDescriptiveQuestionRequest;
import com.pm.axiom.dto.question.CreateMCQQuestionRequest;
import com.pm.axiom.dto.question.QuestionType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateMCQQuestionRequest.class, name = "MCQ"),
        @JsonSubTypes.Type(value = CreateCodingQuestionRequest.class, name = "CODING"),
        @JsonSubTypes.Type(value = CreateDescriptiveQuestionRequest.class, name = "DESCRIPTIVE")
})
public sealed interface CreateQuestionRequest
        permits CreateMCQQuestionRequest, CreateCodingQuestionRequest, CreateDescriptiveQuestionRequest {

    QuestionType type();
    String text();
    Integer marks();
    Integer displayOrder();
}