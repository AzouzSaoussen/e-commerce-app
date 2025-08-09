package com.azouz.ecommerce.notification;

public interface NotificationHandler<T> {
    void handle(T message);
}
