package com.azouz.ecommerce.payment.service;

import com.azouz.ecommerce.notification.NotificationProducer;
import com.azouz.ecommerce.notification.PaymentNotificationRequest;
import com.azouz.ecommerce.payment.dto.PaymentRequest;
import com.azouz.ecommerce.payment.mapper.PaymentMapper;
import com.azouz.ecommerce.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer producer;
    @Override
    public Integer createPayment(PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request));
        producer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        request.amount(),
                        request.method(),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email()
                )
        );
        return payment.getId();
    }
}
