package com.hms.doctor_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MedicalHistoryRequest {

    @NotBlank
    private String diagnosis;

    @NotBlank
    private String treatment;

    private String notes;
}
