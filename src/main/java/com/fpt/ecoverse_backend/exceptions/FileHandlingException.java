package com.fpt.ecoverse_backend.exceptions;

public class FileHandlingException extends RuntimeException{
    public FileHandlingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileHandlingException(String message) {
        super(message);
    }
}
