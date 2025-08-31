package com.hms.admin_service.service;

import com.hms.admin_service.dto.BillRequest;
import com.hms.admin_service.dto.BillResponse;
import com.hms.admin_service.dto.DoctorRequest;
import com.hms.admin_service.dto.DoctorResponse;
import com.hms.admin_service.dto.PatientRequest;
import com.hms.admin_service.dto.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExternalServiceClient {
    // Doctor Service
    Page<DoctorResponse> listDoctors(Pageable pageable);
    DoctorResponse createDoctor(DoctorRequest request);
    // Patient Service
    Page<PatientResponse> listPatients(Pageable pageable);
    PatientResponse createPatient(PatientRequest request);
    // Billing Service
    Page<BillResponse> listBills(Pageable pageable);
    BillResponse createBill(BillRequest request);
}
