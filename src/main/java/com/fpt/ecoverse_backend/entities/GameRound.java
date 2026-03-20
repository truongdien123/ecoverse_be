package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "game_rounds", indexes = {
    @Index(name = "idx_game_rounds_partner_id", columnList = "partner_id"),
    @Index(name = "idx_game_rounds_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRound extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "shared", nullable = false, columnDefinition = "boolean default false")
    private Boolean shared = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by", length = 50, columnDefinition = "varchar(50) default 'PARTNERSHIP'")
    private CreatedBy createdBy = CreatedBy.PARTNERSHIP;

    @Column(name = "item_count", nullable = false)
    private Integer itemCount;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "gameRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameRoundItem> gameRoundItems;

    @OneToMany(mappedBy = "gameRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameAttempt> gameAttempts;

    @OneToMany(mappedBy = "gameRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionLink> competitionLinks;
}
