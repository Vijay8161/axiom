package com.pm.axiom.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "sections",
        indexes = @Index(name = "idx_section_assessment_id", columnList = "assessment_id")
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
}