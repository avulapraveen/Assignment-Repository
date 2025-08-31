package com.hms.audit_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.audit_service.dto.AuditLogRequest;
import com.hms.audit_service.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditEventListener.class);

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"audit-events"}, groupId = "audit_service_group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
        try {
            AuditLogRequest auditLogRequest = objectMapper.readValue(message, AuditLogRequest.class);
            auditLogService.saveAuditLog(auditLogRequest);
            logger.info("Audit log saved: {}", auditLogRequest);
        } catch (Exception e) {
            logger.error("Failed to process audit event: {}, error: {}", message, e.getMessage());
            // Add retry or dead letter topic policies as needed
        }
    }
}
