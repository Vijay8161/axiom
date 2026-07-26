package com.pm.axiom.mapper;

import com.pm.axiom.dto.recruiter.RecruiterResponse;
import com.pm.axiom.entity.Recruiter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruiterMapper {
    RecruiterResponse toResponse(Recruiter recruiter);
}
