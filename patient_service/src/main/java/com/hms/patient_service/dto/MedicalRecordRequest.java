package com.hms.patient_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MedicalRecordRequest {

    private String id;
    private Long patientId;
    private String diagnosis;
    private String treatment;
    private LocalDateTime recordDate;
    private String notes;
}
