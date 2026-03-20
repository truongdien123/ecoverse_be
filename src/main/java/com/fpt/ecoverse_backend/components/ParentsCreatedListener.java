package com.fpt.ecoverse_backend.components;

import com.fpt.ecoverse_backend.dtos.ParentsCreatedEvent;
import com.fpt.ecoverse_backend.services.MailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ParentsCreatedListener {

    private final MailService mailService;

    public ParentsCreatedListener(MailService mailService) {
        this.mailService = mailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ParentsCreatedEvent event) {
        mailService.sendParentWelcomeBatch(event.getMails());
    }
}
