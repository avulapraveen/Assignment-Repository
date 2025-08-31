package com.hms.doctor_service.mapper;

import com.hms.doctor_service.dto.DoctorRequest;
import com.hms.doctor_service.dto.DoctorResponse;
import com.hms.doctor_service.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequest dto) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        // Password should be encoded in service layer
        doctor.setPassword(dto.getPassword());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPhoneNumber(dto.getPhone());
        return doctor;
    }

    public DoctorResponse toDto(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setEmail(doctor.getEmail());
        response.setSpecialization(doctor.getSpecialization());
        response.setPhone(doctor.getPhoneNumber());
        return response;
    }
}