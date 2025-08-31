package com.hms.patient_service.mapper;

import com.hms.patient_service.dto.PatientRequest;
import com.hms.patient_service.dto.PatientResponse;
import com.hms.patient_service.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequest dto) {
        Patient patient = new Patient();
        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        patient.setPassword(dto.getPassword());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setAddress(dto.getAddress());
        return patient;
    }

    public PatientResponse toResponse(Patient patient) {
        PatientResponse resp = new PatientResponse();
        resp.setId(patient.getId());
        resp.setFullName(patient.getFullName());
        resp.setEmail(patient.getEmail());
        resp.setPhoneNumber(patient.getPhoneNumber());
        resp.setAddress(patient.getAddress());
        return resp;
    }
}

