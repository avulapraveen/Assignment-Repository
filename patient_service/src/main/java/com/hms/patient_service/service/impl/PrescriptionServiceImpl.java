package com.hms.patient_service.service.impl;

import com.hms.patient_service.dto.PrescriptionRequest;
import com.hms.patient_service.model.Prescription;
import com.hms.patient_service.repository.PrescriptionRepository;
import com.hms.patient_service.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Override
    public Page<Prescription> getPrescriptions(Long patientId, Pageable pageable) {
        return prescriptionRepository.findByPatientId(patientId, pageable);
    }

    @Override
    public Prescription createPrescription(Long patientId, PrescriptionRequest request) {
        Prescription prescription = Prescription.builder()
                .patientId(patientId)
                .doctorId(request.getDoctorId())
                .medication(request.getMedication())
                .dosage(request.getDosage())
                .instructions(request.getInstructions())
                .issueDate(LocalDateTime.now())
                .build();

        prescription.setIssueDate(java.time.LocalDateTime.now());
        return prescriptionRepository.save(prescription);
    }
}
