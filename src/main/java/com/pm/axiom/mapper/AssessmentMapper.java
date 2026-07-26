package com.pm.axiom.mapper;

import com.pm.axiom.dto.assessment.AssessmentResponse;
import com.pm.axiom.entity.Assessment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    @Mapping(source = "assessment.createdBy.id", target = "createdById")
    @Mapping(source = "assessment.createdBy.name", target = "createdByName")
    @Mapping(source = "questionCount", target = "questionCount")
    AssessmentResponse toResponse(Assessment assessment, long questionCount);
}
