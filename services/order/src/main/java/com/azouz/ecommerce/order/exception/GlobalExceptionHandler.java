package com.azouz.ecommerce.order.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final Map<String, String> VALIDATION_MESSAGES = Map.of(
            "108", "Order amount should be positive",
            "109", "Method…",
            "110", "Customer should be present",
            "111", "You should at least purchase one product"
    );

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exp){
        log.error(exp.getErrorCode().getDescription());
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(exp.getErrorCode().getCode())
                        .businessErrorDescription(exp.getErrorCode().getDescription())
                        .build());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException (EntityNotFoundException exp){
        log.error(exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(exp.hashCode())
                        .businessErrorDescription(exp.getMessage())
                        .error("Entity Not Found exception")
                        .build()
                );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorCode = error.getDefaultMessage(); // This is the code like "100"
                    var friendlyMessage = VALIDATION_MESSAGES.getOrDefault(errorCode, "Invalid value");
                    errors.add(friendlyMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error("An unexpected error occurred. Please contact support.")
                        .build());
    }
}
