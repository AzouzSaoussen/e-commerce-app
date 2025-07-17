package com.azouz.ecommerce.product.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Map<String, String> VALIDATION_MESSAGES = Map.of(
            "104", "Product name must not be blank",
            "105", "Available quantity must be positive",
            "106", "Product price must positive",
            "107", "Product category must not be blank"
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorCode = error.getDefaultMessage();
                    var friendlyMessage = VALIDATION_MESSAGES.getOrDefault(errorCode, "Invalid Value");
                    errors.add(friendlyMessage);
                });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleBusinessExceptions(RuntimeException exp) {
        BusinessErrorCodes errorCode;
        if (exp instanceof ProductNotFoundException) {
            errorCode = ((ProductNotFoundException) exp).getErrorCode();
        } else if (exp instanceof CategoryNotFoundException) {
            errorCode = ((CategoryNotFoundException) exp).getErrorCode();
        } else {
            errorCode = BusinessErrorCodes.NO_CODE;
        }
        logger.warn("{}: {}", exp.getClass().getSimpleName(), errorCode.getDescription());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(errorCode.getCode())
                        .businessErrorDescription(errorCode.getDescription())
                        .build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        logger.error("Unhandled exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error("An unexpected error occurred. Please contact support.")
                        .build());
    }
}