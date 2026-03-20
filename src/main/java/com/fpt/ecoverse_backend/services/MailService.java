package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.ParentCredentialMail;

import java.util.List;

public interface MailService {
    void sendParentWelcomeBatch(List<ParentCredentialMail> mails);
}
