package com.pm.axiom.repository;

import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Recruiter> findAllByRole(Role role);
    Optional<Recruiter> findByIdAndRole(Long id, Role role);

}
