package com.fpt.ecoverse_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ParentsCreatedEvent {
    private final List<ParentCredentialMail> mails;
}
