package com.ansysan.project.notification_service.service;

import com.ansysan.project.notification_service.dto.NotificationRequest;
import com.ansysan.project.notification_service.handler.EmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(NotificationRequest notificationRequest) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(notificationRequest.getTo());
        mailMessage.setSubject(notificationRequest.getSubject());
        mailMessage.setText(notificationRequest.getBody());
        try {
            javaMailSender.send(mailMessage);
            log.error("Email sent to {}", notificationRequest.getTo());
        } catch (Exception e) {
            throw new EmailException("Error during email sending");
        }
    }
}