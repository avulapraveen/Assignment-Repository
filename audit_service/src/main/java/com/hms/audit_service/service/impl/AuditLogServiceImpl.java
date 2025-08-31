package com.hms.audit_service.service.impl;

import com.hms.audit_service.dto.AuditLogRequest;
import com.hms.audit_service.dto.AuditLogResponse;
import com.hms.audit_service.entity.AuditLog;
import com.hms.audit_service.mapper.AuditLogMapper;
import com.hms.audit_service.repository.AuditLogRepository;
import com.hms.audit_service.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public AuditLogResponse saveAuditLog(AuditLogRequest request) {
        AuditLog auditLog = auditLogMapper.toEntity(request);
        AuditLog saved = auditLogRepository.save(auditLog);
        return auditLogMapper.toDto(saved);
    }

    @Override
    public Page<AuditLogResponse> getAuditLogsByUser(String userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable).map(auditLogMapper::toDto);
    }

    @Override
    public Page<AuditLogResponse> getAuditLogsByEntity(String entityType, String entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable).map(auditLogMapper::toDto);
    }
}
