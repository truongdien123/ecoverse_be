package com.fpt.ecoverse_backend.dtos;

import com.fpt.ecoverse_backend.entities.RedemptionRequest;

public class RedemptionCreatedEvent {
    private final RedemptionRequest redemptionRequest;

    public RedemptionCreatedEvent(RedemptionRequest redemptionRequest) {
        this.redemptionRequest = redemptionRequest;
    }

    public RedemptionRequest getRedemptionRequest() {
        return redemptionRequest;
    }
}
