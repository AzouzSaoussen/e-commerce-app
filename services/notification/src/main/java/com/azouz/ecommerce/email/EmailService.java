package com.azouz.ecommerce.email;

import jakarta.mail.MessagingException;

import java.math.BigDecimal;

public interface EmailService {
    void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference) throws MessagingException;

}
