package com.ansysan.project.notification_service.service;

import com.ansysan.project.notification_service.event.UserEventDto;

public interface NotificationService {

    void send(UserEventDto user, String message);

}
