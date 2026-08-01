package com.pm.axiom.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "coding_questions")
@DiscriminatorValue("CODING")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CodingQuestion extends Question {

    // Placeholder fields — refine when this type is actually built out.
    @Column(name = "starter_code", length = 4000)
    private String starterCode;

    @Column(length = 50)
    private String language;
}