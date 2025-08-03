package com.ansysan.project.authentication_service.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.data.channel.notification.name}")
    private String notificationChannel;

    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic(notificationChannel, 1, (short) 1);
    }
}
