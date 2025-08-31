package com.hms.audit_service.mapper;

import com.hms.audit_service.dto.AuditLogRequest;
import com.hms.audit_service.dto.AuditLogResponse;
import com.hms.audit_service.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLog toEntity(AuditLogRequest dto) {
        return AuditLog.builder()
                .eventType(dto.getEventType())
                .entityId(dto.getEntityId())
                .entityType(dto.getEntityType())
                .userId(dto.getUserId())
                .userRole(dto.getUserRole())
                .details(dto.getDetails())
                .eventTime(dto.getEventTime())
                .traceId(dto.getTraceId())
                .build();
    }

    public AuditLogResponse toDto(AuditLog auditLog) {
        AuditLogResponse dto = new AuditLogResponse();
        dto.setId(auditLog.getId());
        dto.setEventType(auditLog.getEventType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setEntityType(auditLog.getEntityType());
        dto.setUserId(auditLog.getUserId());
        dto.setUserRole(auditLog.getUserRole());
        dto.setDetails(auditLog.getDetails());
        dto.setEventTime(auditLog.getEventTime());
        dto.setTraceId(auditLog.getTraceId());
        return dto;
    }
}
