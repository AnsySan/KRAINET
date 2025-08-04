package com.ansysan.project.notification_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventDto {
    private String username;
    private String email;
    private String password;
    private ActionType actionType;
}
