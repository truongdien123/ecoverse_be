package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions", indexes = {
    @Index(name = "idx_questions_template_id", columnList = "quiz_template_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseEntity {

    @Column(name = "text", nullable = false, columnDefinition = "text")
    private String text;

    @Column(name = "options_json", columnDefinition = "text")
    private String optionsJson;

    @Column(name = "correct_answer", nullable = false, length = 255)
    private String correctAnswer;

    @Column(name = "explanation", columnDefinition = "text")
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "quiz_template_id")
    private QuizTemplate quizTemplate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizPlacement> quizPlacements;
}
