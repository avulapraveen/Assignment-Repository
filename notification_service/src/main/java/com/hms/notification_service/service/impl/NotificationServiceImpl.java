package com.hms.notification_service.service.impl;

import com.hms.notification_service.dto.NotificationResponse;
import com.hms.notification_service.entity.Notification;
import com.hms.notification_service.entity.NotificationChannel;
import com.hms.notification_service.entity.UserType;
import com.hms.notification_service.mapper.NotificationMapper;
import com.hms.notification_service.repository.NotificationRepository;
import com.hms.notification_service.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String AUDIT_TOPIC = "audit-events";

    @Override
    public void sendNotification(Long userId, String userType, String message, String channel) {
        Notification notification = Notification.builder()
                .userId(userId)
                .userType(UserType.valueOf(userType))
                .message(message)
                .channel(NotificationChannel.valueOf(channel))
                .sentAt(LocalDateTime.now())
                .readFlag(false)
                .build();

        repository.save(notification);

        // Simulate sending email/SMS (can integrate actual services here)
        System.out.printf("Sent %s notification to %s %d: %s%n", channel, userType, userId, message);

        // Publish audit log
        String auditMsg = String.format("{\"event\":\"NotificationSent\",\"userId\":%d,\"userType\":\"%s\",\"channel\":\"%s\"}",
                userId, userType, channel);
        kafkaTemplate.send(AUDIT_TOPIC, auditMsg);
    }

    @Override
    public Page<NotificationResponse> getNotifications(Long userId, String userType, Pageable pageable) {
        return repository.findByUserIdAndUserType(userId, userType, pageable)
                .map(mapper::toDto);
    }
}
