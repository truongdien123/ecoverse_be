package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_round_items", indexes = {
    @Index(name = "idx_game_round_items_round", columnList = "game_round_id"),
    @Index(name = "idx_game_round_items_order", columnList = "game_round_id,order_index")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoundItem extends BaseEntity {

    @Column(name = "order_index", columnDefinition = "integer default 0")
    private Integer orderIndex = 0;

    @ManyToOne
    @JoinColumn(name = "game_round_id")
    private GameRound gameRound;

    @ManyToOne
    @JoinColumn(name = "waste_item_id")
    private WasteItem wasteItem;
}
