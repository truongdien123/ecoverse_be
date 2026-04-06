package com.fpt.ecoverse_backend.exceptions;

import org.springframework.http.HttpStatus;

public class MethodArgumentTypeMismatchException extends RuntimeException {
    private HttpStatus status;

    public MethodArgumentTypeMismatchException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
