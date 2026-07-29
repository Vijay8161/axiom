package com.pm.axiom.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@Table(
        name = "questions",
        indexes = {
                @Index(name = "idx_question_section_id", columnList = "section_id"),
                @Index(name = "idx_question_created_by", columnList = "created_by")
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private Integer marks;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Difficulty difficulty;

    @Column(length = 2000)
    private String explanation;
}