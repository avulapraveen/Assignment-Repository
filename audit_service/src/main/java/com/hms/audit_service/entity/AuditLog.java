package com.hms.audit_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;
    private String eventType;
    private String entityId;
    private String entityType;
    private String userId;
    private String userRole;
    private String details; // JSON or text
    private LocalDateTime eventTime;
    private String traceId;
}
