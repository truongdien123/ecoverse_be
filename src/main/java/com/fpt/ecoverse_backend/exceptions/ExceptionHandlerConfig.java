package com.fpt.ecoverse_backend.exceptions;

import com.fpt.ecoverse_backend.base.ApiResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerConfig {
    private static final Log LOG = LogFactory.getLog(ExceptionHandlerConfig.class);

    private final MessageSource messageSource;

    public ExceptionHandlerConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(NotFoundException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(message));
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(message));
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<ApiResponse<?>> handleForbiddenException(ForbiddenException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(message));
    }

    @ExceptionHandler(value = FileHandlingException.class)
    public ResponseEntity<ApiResponse<?>> handleFileException(FileHandlingException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ApiResponse.error(message));
    }
}

