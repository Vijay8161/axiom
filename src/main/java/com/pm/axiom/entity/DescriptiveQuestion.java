package com.pm.axiom.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "descriptive_questions")
@DiscriminatorValue("DESCRIPTIVE")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DescriptiveQuestion extends Question {

    // Placeholder field — refine when this type is actually built out.
    @Column(name = "max_word_count")
    private Integer maxWordCount;
}