package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "reward_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardItem extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "points_required", nullable = false)
    private Integer pointsRequired;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "available", nullable = false, columnDefinition = "boolean default true")
    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "rewardItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RedemptionRequest> redemptionRequests;


}