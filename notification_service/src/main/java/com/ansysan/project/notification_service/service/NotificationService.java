package com.ansysan.project.notification_service.service;

import com.ansysan.project.notification_service.dto.NotificationRequest;

public interface NotificationService {

    void send(NotificationRequest notificationRequest);

}
