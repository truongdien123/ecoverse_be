package com.fpt.ecoverse_backend.dtos;

import lombok.Getter;

@Getter
public class PartnerStatusChangedEvent {
    private String partnerId;
    private String email;
    private String status;

    public PartnerStatusChangedEvent(String partnerId, String email, String status) {
        this.partnerId = partnerId;
        this.email = email;
        this.status = status;
    }

}
