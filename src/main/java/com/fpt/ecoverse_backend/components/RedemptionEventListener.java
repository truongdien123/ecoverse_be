package com.fpt.ecoverse_backend.components;

import com.fpt.ecoverse_backend.dtos.RedemptionCreatedEvent;
import com.fpt.ecoverse_backend.entities.RedemptionRequest;
import com.fpt.ecoverse_backend.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RedemptionEventListener {

    private final MailService mailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void handleRedemptionCreated(RedemptionCreatedEvent event) {
        RedemptionRequest rr = event.getRedemptionRequest();

        mailService.sendRewardRequestEmail(
                rr.getParent().getUser().getEmail(),
                rr.getStudent().getUser().getFullName(),
                rr.getRewardItem().getName(),
                rr.getRewardItem().getPointsRequired(),
                LocalDateTime.now().toString()
        );
    }
}
