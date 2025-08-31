package com.hms.patient_service.service;

import com.hms.patient_service.dto.MedicalRecordRequest;
import com.hms.patient_service.dto.MedicalRecordResponse;
import com.hms.patient_service.dto.PatientRequest;
import com.hms.patient_service.dto.PatientResponse;
import com.hms.patient_service.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientResponse registerPatient(PatientRequest patientRequest);
    Optional<PatientResponse> getPatientByEmail(String email);
    Optional<PatientResponse> getPatientById(Long id);
    Page<Patient> listPatients(Pageable pageable);
    Patient updateProfile(Long id, PatientRequest patientRequest);
}