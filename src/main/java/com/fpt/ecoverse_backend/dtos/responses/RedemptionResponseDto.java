package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionResponseDto {

    @JsonProperty("redemption_id")
    private String redemptionId;

    @JsonProperty("studentName")
    private String studentName;

    @JsonProperty("reward_item_name")
    private String rewardItemName;

    @JsonProperty("points_required")
    private Integer pointsRequired;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("redemption_date")
    private String redemptionDate;

    @JsonProperty("parent_approval")
    private ApprovalStatus parentApproval;

    @JsonProperty("partner_approval")
    private ApprovalStatus partnerApproval;

    @JsonProperty("reason_parent")
    private String reasonParent;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("image_reward_item")
    private String imageRewardItem;

    @JsonProperty("reason_partner")
    private String reasonPartner;

    @JsonProperty("fulfilled")
    private Boolean fulfilled;
}
