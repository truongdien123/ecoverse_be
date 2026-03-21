package com.fpt.ecoverse_backend.exceptions;

import com.fpt.ecoverse_backend.utils.ResponseUtil;
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
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseUtil.error(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseUtil.error(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(value = FileHandlingException.class)
    public ResponseEntity<?> handleFileException(FileHandlingException ex) {
        LOG.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message);
    }
}

