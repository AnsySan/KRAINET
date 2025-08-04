package com.ansysan.project.notification_service.service;

import com.ansysan.project.notification_service.dto.UserDto;
import com.ansysan.project.notification_service.event.UserEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void send(UserEventDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Notification");
        mailMessage.setText(message);

        try {
            javaMailSender.send(mailMessage);
            log.info("Message sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Error during email sending");
        }
    }
}