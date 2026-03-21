package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.CompetitionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competition_game_links", indexes = {
    @Index(name = "idx_competition_game_links_competition", columnList = "competition_id"),
    @Index(name = "idx_competition_game_links_game", columnList = "game_round_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionLink extends BaseEntity {

    @Column(name = "is_custom", nullable = false, columnDefinition = "boolean default false")
    private Boolean isCustom = false;

    @Column(name = "score")
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "competition_type", length = 50)
    private CompetitionType competitionType;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "game_round_id")
    private GameRound gameRound;

    @ManyToOne
    @JoinColumn(name = "quiz_template_id")
    private QuizTemplate quizTemplate;
}
