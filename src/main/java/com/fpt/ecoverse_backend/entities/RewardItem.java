package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reward_items")
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

    public RewardItem() {
    }

    public RewardItem(String name, String description, Integer pointsRequired, String imageUrl, Boolean available, Partner partner, List<RedemptionRequest> redemptionRequests) {
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.imageUrl = imageUrl;
        this.available = available;
        this.partner = partner;
        this.redemptionRequests = redemptionRequests;
    }

    public RewardItem(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String description, Integer pointsRequired, String imageUrl, Boolean available, Partner partner, List<RedemptionRequest> redemptionRequests) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.imageUrl = imageUrl;
        this.available = available;
        this.partner = partner;
        this.redemptionRequests = redemptionRequests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public List<RedemptionRequest> getRedemptionRequests() {
        return redemptionRequests;
    }

    public void setRedemptionRequests(List<RedemptionRequest> redemptionRequests) {
        this.redemptionRequests = redemptionRequests;
    }
}
