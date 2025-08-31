package com.hms.billing_service.mapper;

import com.hms.billing_service.dto.BillRequest;
import com.hms.billing_service.dto.BillResponse;
import com.hms.billing_service.entity.Bill;
import com.hms.billing_service.entity.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {

    public Bill toEntity(BillRequest request) {
        return Bill.builder()
                .patientId(request.getPatientId())
                .appointmentId(request.getAppointmentId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }

    public BillResponse toDto(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setPatientId(bill.getPatientId());
        response.setAppointmentId(bill.getAppointmentId());
        response.setAmount(bill.getAmount());
        response.setPaymentMethod(bill.getPaymentMethod());
        response.setStatus(bill.getStatus().name());
        response.setCreatedAt(bill.getCreatedAt());
        response.setPaidAt(bill.getPaidAt());
        return response;
    }
}
