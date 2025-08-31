package com.hms.notification_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;
    private Long userId;
    private String userType;
    private String message;
    private String channel;
    private LocalDateTime sentAt;
    private boolean readFlag;
}
