package com.azouz.ecommerce.kafka.order;

import com.azouz.ecommerce.notification.Notification;
import com.azouz.ecommerce.notification.NotificationHandler;
import com.azouz.ecommerce.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.azouz.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;

@Service
@RequiredArgsConstructor
public class OrderNotificationHandler implements NotificationHandler<OrderConfirmation> {

    private final NotificationRepository repository;

    @Override
    public void handle(OrderConfirmation message) {
        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(message)
                        .build()
        );
        // email sending logic
    }
}
