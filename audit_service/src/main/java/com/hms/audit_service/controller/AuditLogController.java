package com.hms.audit_service.controller;

import com.hms.audit_service.dto.AuditLogResponse;
import com.hms.audit_service.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs Service", description = "Retrieve audit logs for compliance and reporting")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(summary = "Get audit logs by user id with pagination")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AuditLogResponse>> getLogsByUser(@PathVariable String userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Page<AuditLogResponse> logs = auditLogService.getAuditLogsByUser(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(logs);
    }

    @Operation(summary = "Get audit logs by entity with pagination")
    @GetMapping("/entity")
    public ResponseEntity<Page<AuditLogResponse>> getLogsByEntity(@RequestParam String entityType,
                                                                  @RequestParam String entityId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<AuditLogResponse> logs = auditLogService.getAuditLogsByEntity(entityType, entityId, PageRequest.of(page, size));
        return ResponseEntity.ok(logs);
    }
}
