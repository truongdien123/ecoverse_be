package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "waste_items", indexes = {
    @Index(name = "idx_waste_items_bin_code", columnList = "correct_bin_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteItem extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by", length = 50, columnDefinition = "varchar(50) default 'PARTNERSHIP'")
    private CreatedBy createdBy = CreatedBy.PARTNERSHIP;

    @ManyToOne
    @JoinColumn(name = "correct_bin_code")
    private WasteBin wasteBin;

    @OneToMany(mappedBy = "wasteItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameRoundItem> gameRoundItems;

    @OneToMany(mappedBy = "wasteItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePlacement> gamePlacements;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}
