package com.azouz.ecommerce.product.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    PRODUCT_NOT_FOUND(1003, HttpStatus.CONFLICT, "Product not found"),
    CATEGORY_NOT_FOUND(1004, HttpStatus.NOT_FOUND, "Category not found"),
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
}
