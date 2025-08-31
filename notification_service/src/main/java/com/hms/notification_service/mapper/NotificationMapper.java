package com.hms.notification_service.mapper;

import com.hms.notification_service.dto.NotificationResponse;
import com.hms.notification_service.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toDto(Notification notification) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setUserType(notification.getUserType().name());
        dto.setMessage(notification.getMessage());
        dto.setChannel(notification.getChannel().name());
        dto.setSentAt(notification.getSentAt());
        dto.setReadFlag(notification.isReadFlag());
        return dto;
    }
}
