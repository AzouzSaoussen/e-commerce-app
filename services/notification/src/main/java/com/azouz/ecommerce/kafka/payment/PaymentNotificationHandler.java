package com.azouz.ecommerce.kafka.payment;

import com.azouz.ecommerce.email.EmailService;
import com.azouz.ecommerce.notification.Notification;
import com.azouz.ecommerce.notification.NotificationHandler;
import com.azouz.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.azouz.ecommerce.notification.NotificationType.PAYMENT_CONFIRMATION;

@Service
@RequiredArgsConstructor
public class PaymentNotificationHandler implements NotificationHandler<PaymentConfirmation> {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @Override
    public void handle(PaymentConfirmation message) throws MessagingException {
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(message)
                        .build()
        );

        var customerName = message.customerFirstname()+ " " + message.customerLastname();
        emailService.sendPaymentSuccessEmail(
                message.customerEmail(),
                customerName,
                message.amount(),
                message.orderReference()
        );
    }
}