package com.pm.axiom.repository;

import com.pm.axiom.entity.MCQQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MCQQuestionRepository extends JpaRepository<MCQQuestion, Long> {

    List<MCQQuestion> findAllByAssessmentIdOrderByIdAsc(Long assessmentId);

    long countByAssessmentId(Long assessmentId);

    @Query("SELECT q FROM MCQQuestion q JOIN FETCH q.assessment a JOIN FETCH a.createdBy WHERE q.id = :id")
    Optional<MCQQuestion> findByIdWithAssessmentAndCreatedBy(@Param("id") Long id);
}
