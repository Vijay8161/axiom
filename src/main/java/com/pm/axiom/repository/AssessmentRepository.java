package com.pm.axiom.repository;

import com.pm.axiom.entity.Assessment;
import com.pm.axiom.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    @Query("SELECT a FROM Assessment a JOIN FETCH a.createdBy WHERE a.createdBy = :recruiter ORDER BY a.createdAt DESC")
    List<Assessment> findAllByCreatedBy(@Param("recruiter") Recruiter recruiter);

    @Query("SELECT a FROM Assessment a JOIN FETCH a.createdBy ORDER BY a.createdAt DESC")
    List<Assessment> findAllWithCreatedBy();

    @Query("SELECT a FROM Assessment a JOIN FETCH a.createdBy WHERE a.id = :id")
    Optional<Assessment> findByIdWithCreatedBy(@Param("id") Long id);
}