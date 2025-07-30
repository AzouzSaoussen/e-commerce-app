package com.azouz.ecommerce.payment.dto;

import com.azouz.ecommerce.payment.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod method,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}
