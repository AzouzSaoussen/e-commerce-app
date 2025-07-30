package com.azouz.ecommerce.payment.mapper;

import com.azouz.ecommerce.payment.dto.PaymentRequest;
import com.azouz.ecommerce.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request){
        return Payment.builder()
                .id(request.id())
                .amount(request.amount())
                .paymentMethod(request.method())
                .orderId(request.orderId())
                .build();
    }
}
