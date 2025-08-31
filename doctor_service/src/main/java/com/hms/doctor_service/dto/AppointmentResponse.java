package com.hms.doctor_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentTime;
    private String status;
}
