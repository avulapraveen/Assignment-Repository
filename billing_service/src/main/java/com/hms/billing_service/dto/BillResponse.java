package com.hms.billing_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BillResponse {

    private Long id;
    private Long patientId;
    private Long appointmentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
