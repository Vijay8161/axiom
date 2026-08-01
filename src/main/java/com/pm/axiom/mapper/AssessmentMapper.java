package com.pm.axiom.mapper;

import com.pm.axiom.dto.assessment.AssessmentResponse;
import com.pm.axiom.dto.assessment.AssessmentSummaryResponse;
import com.pm.axiom.dto.assessment.CreateAssessmentRequest;
import com.pm.axiom.dto.assessment.UpdateAssessmentRequest;
import com.pm.axiom.entity.Assessment;
import com.pm.axiom.entity.Section;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = SectionMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssessmentMapper {

    Assessment toEntity(CreateAssessmentRequest request);

    void updateEntity(UpdateAssessmentRequest request, @MappingTarget Assessment assessment);

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.name", target = "createdByName")
    AssessmentResponse toResponse(Assessment assessment);

    @Mapping(source = "assessment.createdBy.id", target = "createdById")
    @Mapping(source = "assessment.createdBy.name", target = "createdByName")
    AssessmentSummaryResponse toSummaryResponse(Assessment assessment, int sectionCount, long questionCount);

    @AfterMapping
    default void wireSectionBackReferences(@MappingTarget Assessment assessment) {
        for (Section section : assessment.getSections()) {
            section.setAssessment(assessment);
        }
    }
}