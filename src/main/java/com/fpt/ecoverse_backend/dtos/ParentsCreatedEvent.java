package com.fpt.ecoverse_backend.dtos;

import lombok.Value;

import java.util.List;

@Value
public class ParentsCreatedEvent {
    List<ParentCredentialMail> mails;
}
