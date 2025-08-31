package com.hms.admin_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomAllocationRequest {

    @NotNull
    private Long patientId;
    @NotBlank
    private String roomNumber;
    @NotNull
    private LocalDateTime allocatedAt;
}
