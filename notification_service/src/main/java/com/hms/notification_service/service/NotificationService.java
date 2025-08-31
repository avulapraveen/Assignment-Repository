package com.hms.notification_service.service;

import com.hms.notification_service.dto.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    void sendNotification(Long userId, String userType, String message, String channel);
    Page<NotificationResponse> getNotifications(Long userId, String userType, Pageable pageable);
}
