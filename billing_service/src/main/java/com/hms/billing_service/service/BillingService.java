package com.hms.billing_service.service;

import com.hms.billing_service.dto.BillRequest;
import com.hms.billing_service.dto.BillResponse;

import java.util.List;
import java.util.Optional;

public interface BillingService {

    BillResponse createBill(BillRequest request);
    Optional<BillResponse> getBillById(Long id);
    List<BillResponse> getBillsByPatientId(Long patientId);
    BillResponse updatePaymentStatus(Long id, String status);
}
