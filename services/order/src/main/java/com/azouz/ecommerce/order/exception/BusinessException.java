package com.azouz.ecommerce.order.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    private final BusinessErrorCodes errorCode;
    public BusinessException(BusinessErrorCodes errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
    public BusinessException(BusinessErrorCodes errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

}
