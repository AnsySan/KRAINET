package com.ansysan.project.notification_service.kafka;

import com.ansysan.project.notification_service.config.kafka.KafkaConsumerConfig;
import com.ansysan.project.notification_service.event.UserEventDto;
import com.ansysan.project.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventsConsumer {

    private final NotificationService notificationService;
    private final KafkaConsumerConfig consumer;

    public void consume(UserEventDto userEventDto, String message) {
        notificationService.send(userEventDto, message);
    }
}
