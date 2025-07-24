package com.azouz.ecommerce.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    CUSTOMER_NOT_FOUND(1002, HttpStatus.NOT_FOUND, "Customer not found"),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
    public static BusinessErrorCodes fromHttpStatusCode(int statusCode) {
        return Arrays.stream(BusinessErrorCodes.values())
                .filter(code -> code.getHttpStatus().value() == statusCode)
                .findFirst()
                .orElse(NO_CODE);
    }

}
