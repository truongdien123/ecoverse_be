package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "redemption_requests")
public class RedemptionRequest extends BaseEntity {

    @Column(name = "reason_parent", length = 36)
    private String reasonParent;

    @Column(name = "reason_partner", length = 36)
    private String reasonPartner;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_approval", length = 50, columnDefinition = "varchar(50) default 'PENDING'")
    private ApprovalStatus parentApproval = ApprovalStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "partner_approval", length = 50, columnDefinition = "varchar(50) default 'PENDING'")
    private ApprovalStatus partnerApproval = ApprovalStatus.PENDING;

    @Column(name = "fulfilled", nullable = false, columnDefinition = "boolean default false")
    private Boolean fulfilled = false;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "reward_item_id")
    private RewardItem rewardItem;

    public RedemptionRequest() {
    }

    public RedemptionRequest(String reasonParent, String reasonPartner, ApprovalStatus parentApproval, ApprovalStatus partnerApproval, Boolean fulfilled, Student student, Parent parent, Partner partner, RewardItem rewardItem) {
        this.reasonParent = reasonParent;
        this.reasonPartner = reasonPartner;
        this.parentApproval = parentApproval;
        this.partnerApproval = partnerApproval;
        this.fulfilled = fulfilled;
        this.student = student;
        this.parent = parent;
        this.partner = partner;
        this.rewardItem = rewardItem;
    }

    public RedemptionRequest(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String reasonParent, String reasonPartner, ApprovalStatus parentApproval, ApprovalStatus partnerApproval, Boolean fulfilled, Student student, Parent parent, Partner partner, RewardItem rewardItem) {
        super(id, createdAt, updatedAt);
        this.reasonParent = reasonParent;
        this.reasonPartner = reasonPartner;
        this.parentApproval = parentApproval;
        this.partnerApproval = partnerApproval;
        this.fulfilled = fulfilled;
        this.student = student;
        this.parent = parent;
        this.partner = partner;
        this.rewardItem = rewardItem;
    }

    public String getReasonParent() {
        return reasonParent;
    }

    public void setReasonParent(String reasonParent) {
        this.reasonParent = reasonParent;
    }

    public String getReasonPartner() {
        return reasonPartner;
    }

    public void setReasonPartner(String reasonPartner) {
        this.reasonPartner = reasonPartner;
    }

    public ApprovalStatus getParentApproval() {
        return parentApproval;
    }

    public void setParentApproval(ApprovalStatus parentApproval) {
        this.parentApproval = parentApproval;
    }

    public ApprovalStatus getPartnerApproval() {
        return partnerApproval;
    }

    public void setPartnerApproval(ApprovalStatus partnerApproval) {
        this.partnerApproval = partnerApproval;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public RewardItem getRewardItem() {
        return rewardItem;
    }

    public void setRewardItem(RewardItem rewardItem) {
        this.rewardItem = rewardItem;
    }
}
