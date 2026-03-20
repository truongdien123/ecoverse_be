package com.fpt.ecoverse_backend.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException{
    private HttpStatus status;

    public ForbiddenException(String message, HttpStatus status) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;
    }
}
