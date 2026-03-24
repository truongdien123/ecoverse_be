package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_placements", indexes = {
        @Index(name = "idx_game_placements_attempt", columnList = "game_attempt_id"),
        @Index(name = "idx_game_placements_item", columnList = "waste_item_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GamePlacement extends BaseEntity {

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "game_attempt_id")
    private GameAttempt gameAttempt;

    @ManyToOne
    @JoinColumn(name = "waste_item_id")
    private WasteItem wasteItem;

    @ManyToOne
    @JoinColumn(name = "placed_bin_code")
    private WasteBin placedBin;

}