package com.pm.axiom.dto.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pm.axiom.dto.question.QuestionType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateMCQQuestionRequest.class, name = "MCQ"),
        @JsonSubTypes.Type(value = UpdateCodingQuestionRequest.class, name = "CODING"),
        @JsonSubTypes.Type(value = UpdateDescriptiveQuestionRequest.class, name = "DESCRIPTIVE")
})
public sealed interface UpdateQuestionRequest
        permits UpdateMCQQuestionRequest, UpdateCodingQuestionRequest, UpdateDescriptiveQuestionRequest {

    QuestionType type();
    String text();
    Integer marks();
    Integer displayOrder();
}