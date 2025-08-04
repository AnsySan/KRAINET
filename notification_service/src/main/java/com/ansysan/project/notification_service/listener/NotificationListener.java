package com.ansysan.project.notification_service.listener;

import com.ansysan.project.notification_service.dto.NotificationRequest;
import com.ansysan.project.notification_service.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {
    private final EmailService emailService;
    private final ObjectMapper mapper = new ObjectMapper();

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${spring.data.channel.notification.name}", groupId = "${spring.data.kafka.group-id}")
    public void onMessage(String payload) {
        if (payload == null || payload.trim().isEmpty()) {
            log.error("Received empty or null event");
            return;
        }

        try {
            NotificationRequest message = mapper.readValue(payload, NotificationRequest.class);
            emailService.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}