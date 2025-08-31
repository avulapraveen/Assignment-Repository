package com.hms.appointment_service.service.impl;

import com.hms.appointment_service.dto.AppointmentRequest;
import com.hms.appointment_service.dto.AppointmentResponse;
import com.hms.appointment_service.mapper.AppointmentMapper;
import com.hms.appointment_service.model.Appointment;
import com.hms.appointment_service.model.AppointmentStatus;
import com.hms.appointment_service.repository.AppointmentRepository;
import com.hms.appointment_service.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String APPOINTMENT_EVENTS_TOPIC = "appointment-events";

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Appointment appointment = appointmentMapper.toEntity(request);
        Appointment saved = appointmentRepository.save(appointment);
        publishEvent(AppointmentStatus.SCHEDULED.name(), saved);
        return appointmentMapper.toDto(saved);
    }

    @Override
    public Optional<AppointmentResponse> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(appointmentMapper::toDto);
    }

    @Override
    public Page<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable).map(appointmentMapper::toDto);
    }

    @Override
    public Page<AppointmentResponse> getAppointmentsByPatientId(Long patientId, Pageable pageable) {
        return appointmentRepository.findByPatientId(patientId, pageable).map(appointmentMapper::toDto);
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
            publishEvent("AppointmentCancelled", appointment);
        });
    }

    private void publishEvent(String eventType, Appointment appointment) {
        String event = String.format(
                "{\"eventType\":\"%s\",\"appointmentId\":%d,\"patientId\":%d,\"doctorId\":%d,\"appointmentTime\":\"%s\"}",
                eventType, appointment.getId(), appointment.getPatientId(), appointment.getDoctorId(), appointment.getAppointmentTime().toString());
        kafkaTemplate.send(APPOINTMENT_EVENTS_TOPIC, event);
    }
}
