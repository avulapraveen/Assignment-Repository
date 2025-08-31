package com.hms.audit_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogResponse {

    private String id;
    private String eventType;
    private String entityId;
    private String entityType;
    private String userId;
    private String userRole;
    private String details;
    private LocalDateTime eventTime;
    private String traceId;
}
