package com.hms.patient_service.service.impl;

import com.hms.patient_service.model.MedicalRecord;
import com.hms.patient_service.repository.MedicalRecordRepository;
import com.hms.patient_service.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalHistory) {
        medicalHistory.setRecordDate(java.time.LocalDateTime.now());
        return medicalRecordRepository.save(medicalHistory);
    }

    @Override
    public Page<MedicalRecord> getMedicalRecord(Long patientId, Pageable pageable) {
        return medicalRecordRepository.findByPatientId(patientId, pageable);
    }
}
