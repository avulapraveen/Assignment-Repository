package com.hms.doctor_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalHistoryResponse {

    private String id;
    private Long patientId;
    private String diagnosis;
    private String treatment;
    private LocalDateTime recordDate;
    private String notes;
}
