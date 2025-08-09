package com.azouz.ecommerce.kafka.payment;

import com.azouz.ecommerce.notification.Notification;
import com.azouz.ecommerce.notification.NotificationHandler;
import com.azouz.ecommerce.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.azouz.ecommerce.notification.NotificationType.PAYMENT_CONFIRMATION;

@Service
@RequiredArgsConstructor
public class PaymentNotificationHandler implements NotificationHandler<PaymentConfirmation> {

    private final NotificationRepository repository;

    @Override
    public void handle(PaymentConfirmation message) {
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(message)
                        .build()
        );
        // email sending logic
    }
}