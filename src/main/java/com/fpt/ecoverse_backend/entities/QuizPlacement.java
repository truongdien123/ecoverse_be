package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_placements", indexes = {
        @Index(name = "idx_quiz_placements_attempt", columnList = "quiz_attempt_id"),
        @Index(name = "idx_quiz_placements_question", columnList = "question_id"),
        @Index(name = "idx_quiz_placements_attempt_question", columnList = "quiz_attempt_id,question_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizPlacement extends BaseEntity {

    @Column(name = "selected_answer", length = 255)
    private String selectedAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}