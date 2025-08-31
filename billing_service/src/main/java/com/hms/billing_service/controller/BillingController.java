package com.hms.billing_service.controller;

import com.hms.billing_service.dto.BillRequest;
import com.hms.billing_service.dto.BillResponse;
import com.hms.billing_service.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Tag(name = "Billing Service", description = "Manage billing and payments")
public class BillingController {

    private final BillingService billingService;

    @Operation(summary = "Create a new bill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bill created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/create")
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody BillRequest request) {
        BillResponse bill = billingService.createBill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bill);
    }

    @Operation(summary = "Get bill by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill found"),
            @ApiResponse(responseCode = "404", description = "Bill not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBill(@PathVariable Long id) {
        return billingService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get bills for a patient")
    @ApiResponse(responseCode = "200", description = "List of bills")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillResponse>> getPatientBills(@PathVariable Long patientId) {
        return ResponseEntity.ok(billingService.getBillsByPatientId(patientId));
    }

    @Operation(summary = "Update payment status of a bill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment status updated"),
            @ApiResponse(responseCode = "404", description = "Bill not found")
    })
    @PostMapping("/{id}/update-status")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        billingService.updatePaymentStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}