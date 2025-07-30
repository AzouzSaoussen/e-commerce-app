package com.azouz.ecommerce.payment.service;

import com.azouz.ecommerce.payment.dto.PaymentRequest;

public interface PaymentService {
    Integer createPayment(PaymentRequest request);
}
