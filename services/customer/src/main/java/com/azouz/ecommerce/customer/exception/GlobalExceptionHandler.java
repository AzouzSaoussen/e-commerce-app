package com.azouz.ecommerce.customer.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Map<String, String> VALIDATION_MESSAGES = Map.of(
            "100", "Firstname must not be blank",
            "101", "Lastname must not be blank",
            "102", "Email must not be blank",
            "103", "Address must not be blank"
    );
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

    @ExceptionHandler({CustomerNotFoundException.class, EmailAlreadyExistsException.class})
    public ResponseEntity<ExceptionResponse> handleBusinessExceptions(RuntimeException ex) {
        BusinessErrorCodes errorCode;
        if (ex instanceof CustomerNotFoundException) {
            errorCode = ((CustomerNotFoundException) ex).getErrorCode();
        } else if (ex instanceof EmailAlreadyExistsException) {
            errorCode = ((EmailAlreadyExistsException) ex).getErrorCode();
        } else {
            errorCode = BusinessErrorCodes.NO_CODE;
        }

        logger.warn("{}: {}", ex.getClass().getSimpleName(), errorCode.getDescription());

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
