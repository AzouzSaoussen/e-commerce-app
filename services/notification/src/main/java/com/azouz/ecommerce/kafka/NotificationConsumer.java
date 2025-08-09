package com.azouz.ecommerce.kafka;

import com.azouz.ecommerce.kafka.order.OrderConfirmation;
import com.azouz.ecommerce.kafka.payment.PaymentConfirmation;
import com.azouz.ecommerce.notification.NotificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationHandler<PaymentConfirmation> paymentHandler;
    private final NotificationHandler<OrderConfirmation> orderHandler;

    @KafkaListener(topics = "payment-topic", groupId = "notification-service")
    public void consumePaymentSuccessNotification(PaymentConfirmation confirmation) {
        log.info("Consuming payment message: {}", confirmation);
        paymentHandler.handle(confirmation);
    }

    @KafkaListener(topics = "order-topic", groupId = "notification-service")
    public void consumeOrderSuccessNotification(OrderConfirmation confirmation) {
        log.info("Consuming order message: {}", confirmation);
        orderHandler.handle(confirmation);
    }
}
