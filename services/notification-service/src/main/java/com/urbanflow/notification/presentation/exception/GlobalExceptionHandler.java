package com.urbanflow.notification.presentation.exception;

import com.urbanflow.common.dto.ErrorResponse;
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
                .map(fieldError -> "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
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
    public ResponseEntity<ErrorResponse> handleUrbanFlow(
            UrbanFlowException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        exception.getErrorCode(),
                        exception.getMessage(),
                        request.getRequestURI(),
                        request.getHeader("X-Correlation-Id")
                )
        );
    }
}
