package com.pm.axiom.dto.question;

import com.pm.axiom.entity.Difficulty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MCQQuestionResponse.class, name = "MCQ"),
        @JsonSubTypes.Type(value = CodingQuestionResponse.class, name = "CODING"),
        @JsonSubTypes.Type(value = DescriptiveQuestionResponse.class, name = "DESCRIPTIVE")
})
public sealed interface QuestionResponse
        permits MCQQuestionResponse, CodingQuestionResponse, DescriptiveQuestionResponse {

    Long id();
    QuestionType type();
    String text();
    Integer marks();
    Integer displayOrder();
    Difficulty difficulty();
    String explanation();
}