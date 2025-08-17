package com.azouz.ecommerce.kafka.order;

import com.azouz.ecommerce.email.EmailService;
import com.azouz.ecommerce.notification.Notification;
import com.azouz.ecommerce.notification.NotificationHandler;
import com.azouz.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.azouz.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;

@Service
@RequiredArgsConstructor
public class OrderNotificationHandler implements NotificationHandler<OrderConfirmation> {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @Override
    public void handle(OrderConfirmation message) throws MessagingException {
        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(message)
                        .build()
        );

        var customerName = message.customer().firstname() + " " + message.customer().lastname();
        emailService.sendOrderConfirmationEmail(
                message.customer().email(),
                customerName,
                message.totalAmount(),
                message.orderReference(),
                message.products()
        );
    }
}
