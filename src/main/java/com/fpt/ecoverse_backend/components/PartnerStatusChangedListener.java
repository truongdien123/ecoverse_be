package com.fpt.ecoverse_backend.components;

import com.fpt.ecoverse_backend.dtos.PartnerStatusChangedEvent;
import com.fpt.ecoverse_backend.services.MailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PartnerStatusChangedListener {


    private MailService mailService;

    public PartnerStatusChangedListener(MailService mailService) {
        this.mailService = mailService;
    }


    @EventListener
    public void handlePartnerStatusChanged(PartnerStatusChangedEvent event) {
        String subject = "Trạng thái tài khoản đã được cập nhật";

        mailService.sendPartner(
                event.getEmail(),
                subject,
                event.getStatus()
        );
    }

}
