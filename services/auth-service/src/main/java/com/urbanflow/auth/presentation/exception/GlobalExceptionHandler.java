package com.urbanflow.auth.presentation.exception;

import com.urbanflow.common.dto.ErrorResponse;
import com.urbanflow.common.exception.BusinessException;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.common.exception.UrbanFlowException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = mapBusinessStatus(exception.getErrorCode());
        return ResponseEntity.status(status).body(
                ErrorResponse.of(
                        exception.getErrorCode(),
                        exception.getMessage(),
                        request.getRequestURI(),
                        request.getHeader("X-Correlation-Id")
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.of(
                        exception.getErrorCode(),
                        exception.getMessage(),
                        request.getRequestURI(),
                        request.getHeader("X-Correlation-Id")
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .toList();

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        "VALIDATION_ERROR",
                        "Request validation failed",
                        request.getRequestURI(),
                        request.getHeader("X-Correlation-Id"),
                        details
                )
        );
    }

    @ExceptionHandler(UrbanFlowException.class)
    public ResponseEntity<ErrorResponse> handleUrbanFlowException(
            UrbanFlowException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(
                        exception.getErrorCode(),
                        exception.getMessage(),
                        request.getRequestURI(),
                        request.getHeader("X-Correlation-Id")
                )
        );
    }

    private HttpStatus mapBusinessStatus(String errorCode) {
        return switch (errorCode) {
            case "INVALID_CREDENTIALS", "INVALID_REFRESH_TOKEN" -> HttpStatus.UNAUTHORIZED;
            case "EMAIL_ALREADY_EXISTS" -> HttpStatus.CONFLICT;
            case "USER_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    private String formatFieldError(FieldError fieldError) {
        return "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
