package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "quiz_attempts", indexes = {
        @Index(name = "idx_quiz_attempts_template_id", columnList = "quiz_template_id"),
        @Index(name = "idx_quiz_attempts_student_id", columnList = "student_id"),
        @Index(name = "idx_quiz_attempts_template_student", columnList = "quiz_template_id,student_id"),
        @Index(name = "idx_quiz_attempts_completed", columnList = "completed")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt extends BaseEntity {

    @Column(name = "selected_answers_json", columnDefinition = "text")
    private String selectedAnswersJson;

    @Column(name = "score", columnDefinition = "integer default 0")
    private Integer score = 0;

    @Column(name = "correct_amount")
    private Integer correctAmount;

    @Column(name = "wrong_amount")
    private Integer wrongAmount;

    @Column(name = "completed", nullable = false, columnDefinition = "boolean default false")
    private Boolean completed = false;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "attempt_number", columnDefinition = "integer default 1")
    private Integer attemptNumber = 1;

    @ManyToOne
    @JoinColumn(name = "quiz_template_id")
    private QuizTemplate quizTemplate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizPlacement> quizPlacements;

}