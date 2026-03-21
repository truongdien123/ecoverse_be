package com.fpt.ecoverse_backend.dtos;

import lombok.Value;

@Value
public class ParentCredentialMail {
    String email;
    String fullName;
    String rawPassword;
}
