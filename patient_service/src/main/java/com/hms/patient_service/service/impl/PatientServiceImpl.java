package com.hms.patient_service.service.impl;

import com.hms.patient_service.dto.PatientRequest;
import com.hms.patient_service.dto.PatientResponse;
import com.hms.patient_service.mapper.PatientMapper;
import com.hms.patient_service.model.Patient;
import com.hms.patient_service.repository.MedicalRecordRepository;
import com.hms.patient_service.repository.PatientRepository;
import com.hms.patient_service.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "patient-events";

    @Override
    public PatientResponse registerPatient(PatientRequest patientRequest) {
        Patient patient = patientMapper.toEntity(patientRequest);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponse(saved);
    }

    @Override
    public Optional<PatientResponse> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email).map(patientMapper::toResponse);
    }

    @Override
    public Optional<PatientResponse> getPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toResponse);
    }

    @Override
    public Page<Patient> listPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Patient updateProfile(Long id, PatientRequest patientRequest) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setFullName(patientRequest.getFullName());
        existing.setEmail(patientRequest.getEmail());
        existing.setPhoneNumber(patientRequest.getPhoneNumber());
        existing.setAddress(patientRequest.getAddress());
        Patient saved = patientRepository.save(existing);
        publishPatientEvent("PatientUpdated", saved.getId());
        return saved;
    }

    private void publishPatientEvent(String eventType, Long patientId) {
        String eventJson = String.format("{\"eventType\":\"%s\",\"patientId\":%d}", eventType, patientId);
        kafkaTemplate.send(TOPIC, eventJson);
    }
}
