package com.pm.axiom.repository;

import com.pm.axiom.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Typed against the abstract Question base class — works polymorphically across
 * every concrete question type (MCQ, Coding, Descriptive, and any future subclass)
 * with no per-type repository needed. Hibernate resolves the concrete subtype via
 * the question_type discriminator transparently.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllBySectionIdOrderByDisplayOrderAsc(Long sectionId);

    long countBySectionId(Long sectionId);

    long countBySectionAssessmentId(Long assessmentId);

    @Query("SELECT q FROM Question q " +
            "JOIN FETCH q.section s " +
            "JOIN FETCH s.assessment a " +
            "JOIN FETCH a.createdBy " +
            "WHERE q.id = :id")
    Optional<Question> findByIdWithSectionAndCreatedBy(@Param("id") Long id);
}