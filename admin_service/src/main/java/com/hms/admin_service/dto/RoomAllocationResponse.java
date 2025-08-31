package com.hms.admin_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomAllocationResponse {

    private Long id;
    private Long patientId;
    private String roomNumber;
    private LocalDateTime allocatedAt;
    private LocalDateTime releasedAt;
}
