package com.hms.patient_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MedicalRecordResponse {

    private Long id;
    private String description;
    private LocalDate recordDate;
}
