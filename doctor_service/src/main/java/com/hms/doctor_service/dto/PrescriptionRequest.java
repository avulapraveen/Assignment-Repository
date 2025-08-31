package com.hms.doctor_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionRequest {

    @NotNull
    private Long doctorId;

    @NotBlank
    private String medication;

    @NotBlank
    private String dosage;

    private String instructions;
}
