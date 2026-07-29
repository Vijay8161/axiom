package com.pm.axiom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "mcq_options",
        indexes = @Index(name = "idx_mcq_option_question_id", columnList = "question_id")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McqOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private MCQQuestion question;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;
}