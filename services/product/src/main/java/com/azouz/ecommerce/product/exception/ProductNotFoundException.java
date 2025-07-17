package com.azouz.ecommerce.product.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductNotFoundException extends  RuntimeException{
    private final BusinessErrorCodes errorCode;
    public ProductNotFoundException() {
        super(BusinessErrorCodes.PRODUCT_NOT_FOUND.getDescription());
        this.errorCode = BusinessErrorCodes.PRODUCT_NOT_FOUND;
    }
}
