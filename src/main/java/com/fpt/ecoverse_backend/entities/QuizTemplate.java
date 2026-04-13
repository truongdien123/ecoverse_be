package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "quiz_templates", indexes = {
    @Index(name = "idx_quiz_templates_partner_id", columnList = "partner_id"),
    @Index(name = "idx_quiz_templates_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizTemplate extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by", length = 50)
    private CreatedBy createdBy;

    @Column(name = "is_competition")
    private Boolean isCompetition;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "quizTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @OneToMany(mappedBy = "quizTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAttempt> quizAttempts;

    @OneToMany(mappedBy = "quizTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionLink> competitionLinks;
}
