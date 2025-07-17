package com.azouz.ecommerce.product.exception;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryNotFoundException extends RuntimeException{
    private final BusinessErrorCodes errorCode;
    public CategoryNotFoundException() {
        super(BusinessErrorCodes.CATEGORY_NOT_FOUND.getDescription());
        this.errorCode = BusinessErrorCodes.CATEGORY_NOT_FOUND;
    }

}
