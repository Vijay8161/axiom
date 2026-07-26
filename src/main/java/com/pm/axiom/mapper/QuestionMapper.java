package com.pm.axiom.mapper;

import com.pm.axiom.dto.question.QuestionResponse;
import com.pm.axiom.entity.MCQQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(source = "assessment.id", target = "assessmentId")
    QuestionResponse toResponse(MCQQuestion question);
}
