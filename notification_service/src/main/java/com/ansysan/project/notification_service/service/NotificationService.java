package com.ansysan.project.notification_service.service;

import com.ansysan.project.notification_service.dto.UserDto;

public interface NotificationService {

    void send(UserDto user, String message);

}
