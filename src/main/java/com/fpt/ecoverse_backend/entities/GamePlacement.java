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

    public GamePlacement() {
    }

    public GamePlacement(Boolean isCorrect, GameAttempt gameAttempt, WasteItem wasteItem, WasteBin placedBin) {
        this.isCorrect = isCorrect;
        this.gameAttempt = gameAttempt;
        this.wasteItem = wasteItem;
        this.placedBin = placedBin;
    }

    public GamePlacement(String id, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isCorrect, GameAttempt gameAttempt, WasteItem wasteItem, WasteBin placedBin) {
        super(id, createdAt, updatedAt);
        this.isCorrect = isCorrect;
        this.gameAttempt = gameAttempt;
        this.wasteItem = wasteItem;
        this.placedBin = placedBin;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public GameAttempt getGameAttempt() {
        return gameAttempt;
    }

    public void setGameAttempt(GameAttempt gameAttempt) {
        this.gameAttempt = gameAttempt;
    }

    public WasteItem getWasteItem() {
        return wasteItem;
    }

    public void setWasteItem(WasteItem wasteItem) {
        this.wasteItem = wasteItem;
    }

    public WasteBin getPlacedBin() {
        return placedBin;
    }

    public void setPlacedBin(WasteBin placedBin) {
        this.placedBin = placedBin;
    }
}
