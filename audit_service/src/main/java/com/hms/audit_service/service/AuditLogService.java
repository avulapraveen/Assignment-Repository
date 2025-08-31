package com.hms.audit_service.service;

import com.hms.audit_service.dto.AuditLogRequest;
import com.hms.audit_service.dto.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    AuditLogResponse saveAuditLog(AuditLogRequest request);
    Page<AuditLogResponse> getAuditLogsByUser(String userId, Pageable pageable);
    Page<AuditLogResponse> getAuditLogsByEntity(String entityType, String entityId, Pageable pageable);
}
