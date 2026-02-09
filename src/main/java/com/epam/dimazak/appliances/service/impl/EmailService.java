package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.EmailSendingException;
import com.epam.dimazak.appliances.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String email;

    @Async
    @Loggable
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}", to, e);
            throw new EmailSendingException("Failed to send email");
        }
    }

    @Async
    public void sendOrderStatusChangeNotification(String to, Long orderId, OrderStatus newStatus) {
        String subject = messageSource.getMessage("email.order.status.subject", new Object[]{orderId}, LocaleContextHolder.getLocale());
        String statusText = newStatus.name();
        String body = messageSource.getMessage("email.order.status.body", new Object[]{orderId, statusText}, LocaleContextHolder.getLocale());
        sendSimpleMessage(to, subject, body);
    }
}