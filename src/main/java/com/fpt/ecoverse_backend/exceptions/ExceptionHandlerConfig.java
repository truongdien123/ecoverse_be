package com.fpt.ecoverse_backend.exceptions;

import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        LOG.error(ex.getMessage(), ex);
        return ResponseUtil.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(value = DisabledException.class)
    public ResponseEntity<?> handleDisabled(DisabledException ex) {
        LOG.error(ex.getMessage(), ex);
        return ResponseUtil.error(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        LOG.error(ex.getMessage(), ex);
        return ResponseUtil.error(HttpStatus.FORBIDDEN, "Access denied: " + ex.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        LOG.error(ex.getMessage(), ex);
        return ResponseUtil.error(HttpStatus.UNAUTHORIZED, "Authentication failed: " + ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        LOG.error(ex.getMessage(), ex);
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Invalid parameter type: " + ex.getMessage());
    }
}

