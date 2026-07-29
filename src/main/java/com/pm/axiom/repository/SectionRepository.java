package com.pm.axiom.repository;

import com.pm.axiom.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByAssessmentIdOrderByDisplayOrderAsc(Long assessmentId);

    long countByAssessmentId(Long assessmentId);

    @Query("SELECT s FROM Section s JOIN FETCH s.assessment a JOIN FETCH a.createdBy WHERE s.id = :id")
    Optional<Section> findByIdWithAssessmentAndCreatedBy(@Param("id") Long id);
}