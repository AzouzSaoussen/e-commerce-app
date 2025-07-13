package com.azouz.ecommerce.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailAlreadyExistsException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public EmailAlreadyExistsException() {
        super(BusinessErrorCodes.EMAIL_ALREADY_EXISTS.getDescription());
        this.errorCode = BusinessErrorCodes.EMAIL_ALREADY_EXISTS;
    }
}
