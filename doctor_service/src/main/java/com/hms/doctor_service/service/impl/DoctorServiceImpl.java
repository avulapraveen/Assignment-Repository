package com.hms.doctor_service.service.impl;

import com.hms.doctor_service.dto.AppointmentResponse;
import com.hms.doctor_service.dto.DoctorRequest;
import com.hms.doctor_service.dto.DoctorResponse;
import com.hms.doctor_service.dto.MedicalHistoryRequest;
import com.hms.doctor_service.dto.MedicalHistoryResponse;
import com.hms.doctor_service.dto.PrescriptionRequest;
import com.hms.doctor_service.dto.PrescriptionResponse;
import com.hms.doctor_service.mapper.DoctorMapper;
import com.hms.doctor_service.model.Doctor;
import com.hms.doctor_service.repository.DoctorRepository;
import com.hms.doctor_service.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClient webClient; // configured to call Patient and Appointment services

    private static final String TOPIC = "doctor-events";

    @Override
    public DoctorResponse registerDoctor(DoctorRequest request) {
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        Doctor saved = doctorRepository.save(doctor);
        publishEvent("DoctorRegistered", saved.getId());
        return doctorMapper.toDto(saved);
    }

    @Override
    public Optional<DoctorResponse> getDoctorById(Long id) {
        return doctorRepository.findById(id).map(doctorMapper::toDto);
    }

    @Override
    public Page<DoctorResponse> listDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(doctorMapper::toDto);
    }

    @Override
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setSpecialization(request.getSpecialization());
        existing.setPhoneNumber(request.getPhone());
        Doctor saved = doctorRepository.save(existing);
        publishEvent("DoctorUpdated", saved.getId());
        return doctorMapper.toDto(saved);
    }

    private void publishEvent(String eventType, Long doctorId) {
        String event = String.format("{\"eventType\":\"%s\", \"doctorId\":%d}", eventType, doctorId);
        kafkaTemplate.send(TOPIC, event);
    }

    @Override
    public Page<AppointmentResponse> getAssignedAppointments(Long doctorId, Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        Mono<PageImpl<AppointmentResponse>> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/appointments/doctor/{doctorId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(doctorId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageImpl<AppointmentResponse>>() {});

        return mono.block();  // In production, use reactive flow or async appropriately
    }

    @Override
    public PrescriptionResponse addPrescription(Long patientId, PrescriptionRequest request) {
        return webClient.post()
                .uri("/api/patients/{patientId}/prescriptions", patientId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PrescriptionResponse.class)
                .block();
    }

    @Override
    public MedicalHistoryResponse addDiagnosis(Long patientId, MedicalHistoryRequest request) {
        return webClient.post()
                .uri("/api/patients/{patientId}/medical-record", patientId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MedicalHistoryResponse.class)
                .block();
    }

    @Override
    public Page<MedicalHistoryResponse> getPatientMedicalHistory(Long patientId, Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        Mono<PageImpl<MedicalHistoryResponse>> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/patients/{patientId}/medical-record")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(patientId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return mono.block();
    }
}