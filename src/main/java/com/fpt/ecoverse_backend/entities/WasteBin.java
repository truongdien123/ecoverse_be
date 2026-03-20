package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "waste_bins", indexes = {
    @Index(name = "idx_waste_bins_code", columnList = "code", unique = true),
    @Index(name = "idx_waste_bins_order", columnList = "order_index"),
    @Index(name = "idx_waste_bins_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteBin extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private WasteBinCode code;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "color_hex", nullable = false, length = 7)
    private String colorHex;

    @Column(name = "icon_url", length = 512)
    private String iconUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "order_index", columnDefinition = "integer default 0")
    private Integer orderIndex = 0;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    @OneToMany(mappedBy = "wasteBin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WasteItem> wasteItems;

    @OneToMany(mappedBy = "placedBin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePlacement> gamePlacements;
}
