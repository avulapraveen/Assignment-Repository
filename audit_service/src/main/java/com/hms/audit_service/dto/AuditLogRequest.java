package com.hms.audit_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogRequest {

    @NotBlank
    private String eventType;
    @NotBlank
    private String entityId;
    @NotBlank
    private String entityType;
    private String userId;
    private String userRole;
    private String details;
    @NotNull
    private LocalDateTime eventTime;
    private String traceId;
}
