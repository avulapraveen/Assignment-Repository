package com.hms.billing_service.service.impl;

import com.hms.billing_service.dto.BillRequest;
import com.hms.billing_service.dto.BillResponse;
import com.hms.billing_service.entity.Bill;
import com.hms.billing_service.entity.PaymentStatus;
import com.hms.billing_service.mapper.BillingMapper;
import com.hms.billing_service.repository.BillRepository;
import com.hms.billing_service.service.BillingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {

    private final BillRepository billRepository;
    private final BillingMapper billingMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "billing-events";

    @Override
    public BillResponse createBill(BillRequest request) {
        Bill bill = billingMapper.toEntity(request);
        Bill saved = billRepository.save(bill);
        publishBillingEvent(saved, "BillingCreated");
        return billingMapper.toDto(saved);
    }

    @Override
    public Optional<BillResponse> getBillById(Long id) {
        return billRepository.findById(id).map(billingMapper::toDto);
    }

    @Override
    public List<BillResponse> getBillsByPatientId(Long patientId) {
        List<Bill> bills = billRepository.findByPatientId(patientId);
        return bills.stream().map(billingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BillResponse updatePaymentStatus(Long id, String status) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setStatus(PaymentStatus.valueOf(status));
        if ("PAID".equalsIgnoreCase(status)) {
            bill.setPaidAt(LocalDateTime.now());
        }
        Bill saved = billRepository.save(bill);
        publishBillingEvent(saved, "BillingUpdated");
        return billingMapper.toDto(saved);
    }

    private void publishBillingEvent(Bill bill, String eventType) {
        String event = String.format(
                "{\"eventType\":\"%s\", \"billId\":%d, \"patientId\":%d, \"amount\":%s, \"status\":\"%s\"}",
                eventType, bill.getId(), bill.getPatientId(), bill.getAmount(), bill.getStatus());
        kafkaTemplate.send(TOPIC, event);
    }
}
