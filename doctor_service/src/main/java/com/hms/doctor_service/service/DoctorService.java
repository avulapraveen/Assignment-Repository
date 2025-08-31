package com.hms.doctor_service.service;

import com.hms.doctor_service.dto.AppointmentResponse;
import com.hms.doctor_service.dto.DoctorRequest;
import com.hms.doctor_service.dto.DoctorResponse;
import com.hms.doctor_service.dto.MedicalHistoryRequest;
import com.hms.doctor_service.dto.MedicalHistoryResponse;
import com.hms.doctor_service.dto.PrescriptionRequest;
import com.hms.doctor_service.dto.PrescriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DoctorService {

    DoctorResponse registerDoctor(DoctorRequest request);
    Optional<DoctorResponse> getDoctorById(Long id);
    Page<DoctorResponse> listDoctors(Pageable pageable);
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    Page<AppointmentResponse> getAssignedAppointments(Long doctorId, Pageable pageable);
    PrescriptionResponse addPrescription(Long patientId, PrescriptionRequest request);
    MedicalHistoryResponse addDiagnosis(Long patientId, MedicalHistoryRequest request);
    Page<MedicalHistoryResponse> getPatientMedicalHistory(Long patientId, Pageable pageable);
}
