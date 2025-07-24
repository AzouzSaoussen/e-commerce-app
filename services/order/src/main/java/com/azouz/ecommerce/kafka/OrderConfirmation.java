package com.azouz.ecommerce.kafka;

import com.azouz.ecommerce.customer.CustomerResponse;
import com.azouz.ecommerce.order.model.PaymentMethod;
import com.azouz.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
