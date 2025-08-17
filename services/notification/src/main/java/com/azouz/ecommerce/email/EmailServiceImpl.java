package com.azouz.ecommerce.email;

import com.azouz.ecommerce.exception.EmailSendException;
import com.azouz.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.azouz.ecommerce.email.EmailTemplates.ORDER_CONFIRMATION;
import static com.azouz.ecommerce.email.EmailTemplates.PAYMENT_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_RELATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${application.mail.from}")
    private String senderEmail;

    @Async
    @Override
    public void sendPaymentSuccessEmail(String destinationEmail,
                                        String customerName,
                                        BigDecimal amount,
                                        String orderReference) {
        sendEmail(
                destinationEmail,
                PAYMENT_CONFIRMATION.getSubject(),
                PAYMENT_CONFIRMATION.getTemplate(),
                buildPaymentVariables(customerName, amount, orderReference)
        );
    }

    @Async
    @Override
    public void sendOrderConfirmationEmail(String destinationEmail,
                                           String customerName,
                                           BigDecimal amount,
                                           String orderReference,
                                           List<Product> products) {
        sendEmail(
                destinationEmail,
                ORDER_CONFIRMATION.getSubject(),
                ORDER_CONFIRMATION.getTemplate(),
                buildOrderVariables(customerName, amount, orderReference, products)
        );
    }

    /**
     * Centralized reusable email sending method.
     */
    private void sendEmail(String destinationEmail,
                           String subject,
                           String templateName,
                           Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());

            helper.setFrom(senderEmail);
            helper.setTo(destinationEmail);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("✅ Email [{}] sent successfully to {} using template [{}]",
                    subject, destinationEmail, templateName);

        } catch (MessagingException e) {
            log.error("❌ Failed to send email [{}] to {} using template [{}]",
                    subject, destinationEmail, templateName, e);
            throw new EmailSendException("Failed to send email to " + destinationEmail, e);
        }
    }

    private Map<String, Object> buildPaymentVariables(String customerName,
                                                      BigDecimal amount,
                                                      String orderReference) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("customerName", customerName);
        vars.put("totalAmount", amount);
        vars.put("orderReference", orderReference);
        return vars;
    }

    private Map<String, Object> buildOrderVariables(String customerName,
                                                    BigDecimal amount,
                                                    String orderReference,
                                                    List<Product> products) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("customerName", customerName);
        vars.put("amount", amount);
        vars.put("orderReference", orderReference);
        vars.put("products", products);
        return vars;
    }
}
