package com.azouz.ecommerce.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerNotFoundException extends RuntimeException {
    private final BusinessErrorCodes errorCode;
    public CustomerNotFoundException() {
        super(BusinessErrorCodes.CUSTOMER_NOT_FOUND.getDescription());
        this.errorCode = BusinessErrorCodes.CUSTOMER_NOT_FOUND;
    }
}
