package com.pm.axiom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "recruiters",
        indexes = @Index(name = "idx_recruiter_email", columnList = "email", unique = true)
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Recruiter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    // Nullable: the seeded platform ADMIN has no company.
    @Column(name = "company_name", length = 150)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
