package com.pm.axiom.mapper;

import com.pm.axiom.dto.section.CreateSectionRequest;
import com.pm.axiom.dto.section.SectionResponse;
import com.pm.axiom.dto.section.UpdateSectionRequest;
import com.pm.axiom.entity.Question;
import com.pm.axiom.entity.Section;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = QuestionMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SectionMapper {

    Section toEntity(CreateSectionRequest request);

    void updateEntity(UpdateSectionRequest request, @MappingTarget Section section);

    SectionResponse toResponse(Section section);

    @AfterMapping
    default void wireQuestionBackReferences(@MappingTarget Section section) {
        for (Question question : section.getQuestions()) {
            question.setSection(section);
        }
    }
}