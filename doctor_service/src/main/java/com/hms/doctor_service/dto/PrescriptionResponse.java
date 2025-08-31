package com.hms.doctor_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescriptionResponse {

    private String id;
    private Long patientId;
    private Long doctorId;
    private String medication;
    private String dosage;
    private String instructions;
    private LocalDateTime issueDate;
}
