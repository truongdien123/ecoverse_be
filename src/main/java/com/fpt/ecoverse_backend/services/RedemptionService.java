package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.responses.RedemptionResponseDto;

public interface RedemptionService {
    RedemptionResponseDto createRedemption(String studentId, String rewardItemId);
    RedemptionResponseDto approveRedemptionByParent(String parentId, String redemptionId, boolean approved);
    RedemptionResponseDto approveRedemptionByPartner(String partnerId, String redemptionId, boolean approved);
    RedemptionResponseDto fulfillRedemption(String redemptionId);
}
