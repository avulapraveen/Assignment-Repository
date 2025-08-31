package com.hms.patient_service.service;

import com.hms.patient_service.model.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordService {

    MedicalRecord createMedicalRecord(MedicalRecord medicalHistory);
    Page<MedicalRecord> getMedicalRecord(Long patientId, Pageable pageable);
}
