package com.hms.notification_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.notification_service.service.NotificationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"appointment-events", "billing-events"}, groupId = "notification_service_group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
        try {
            logger.info("Consuming event from Kafka: {}", message);

            EventPayload event = objectMapper.readValue(message, EventPayload.class);

            Long userId = event.getPatientId() != null ? event.getPatientId() : event.getDoctorId();
            String userType = event.getPatientId() != null ? "PATIENT" : "DOCTOR";

            if (userId == null) {
                logger.warn("UserId not present in event: {}", message);
                throw new IllegalArgumentException("UserId is missing in event.");
            }

            notificationService.sendNotification(userId, userType, event.getMessage(), event.getChannel());

            logger.info("Notification sent successfully to {} {}", userType, userId);
        } catch (Exception ex) {
            logger.error("Failed to process Kafka event: {}", message, ex);
            // Exception thrown here triggers retry and eventually dead-letter topic if retries exhausted
            throw new RuntimeException(ex);
        }
    }

    @Data
    public static class EventPayload {
        private Long patientId;
        private Long doctorId;
        private String message;
        private String channel;
    }
}
