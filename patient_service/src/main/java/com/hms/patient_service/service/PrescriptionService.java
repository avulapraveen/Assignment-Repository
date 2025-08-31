package com.hms.patient_service.service;

import com.hms.patient_service.dto.PrescriptionRequest;
import com.hms.patient_service.model.Prescription;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionService {
    Page<Prescription> getPrescriptions(Long patientId, Pageable pageable);
    Prescription createPrescription(Long patientId, @Valid PrescriptionRequest request);
}
