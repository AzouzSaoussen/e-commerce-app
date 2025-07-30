package com.azouz.ecommerce.payment;

import com.azouz.ecommerce.customer.CustomerResponse;
import com.azouz.ecommerce.order.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod method,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
