package com.azouz.ecommerce.notification;

import jakarta.mail.MessagingException;

public interface NotificationHandler<T> {
    void handle(T message) throws MessagingException;
}
