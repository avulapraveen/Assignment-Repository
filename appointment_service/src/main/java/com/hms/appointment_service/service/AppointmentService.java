package com.hms.appointment_service.service;

import com.hms.appointment_service.dto.AppointmentRequest;
import com.hms.appointment_service.dto.AppointmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);
    Optional<AppointmentResponse> getAppointmentById(Long id);
    Page<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId, Pageable pageable);
    Page<AppointmentResponse> getAppointmentsByPatientId(Long patientId, Pageable pageable);
    void cancelAppointment(Long appointmentId);
}