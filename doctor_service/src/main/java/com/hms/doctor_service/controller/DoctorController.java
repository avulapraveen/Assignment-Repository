package com.hms.doctor_service.controller;

import com.hms.doctor_service.dto.*;
import com.hms.doctor_service.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Service", description = "Manage doctor profiles and workflows")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Register new doctor",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Doctor registered successfully", content = @Content(schema = @Schema(implementation = DoctorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/register")
    public ResponseEntity<DoctorResponse> register(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.registerDoctor(request));
    }

    @Operation(summary = "Get doctor by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor found", content = @Content(schema = @Schema(implementation = DoctorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Doctor not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List doctors with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of doctors")
            })
    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> listDoctors(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorService.listDoctors(PageRequest.of(page, size)));
    }

    @Operation(summary = "Update doctor profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor updated", content = @Content(schema = @Schema(implementation = DoctorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Doctor not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateProfile(@PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @Operation(summary = "Get assigned appointments for a doctor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of appointments")
            })
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<Page<AppointmentResponse>> getAssignedAppointments(@PathVariable Long doctorId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorService.getAssignedAppointments(doctorId, PageRequest.of(page, size)));
    }

    @Operation(summary = "Add prescription for a patient",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Prescription added", content = @Content(schema = @Schema(implementation = PrescriptionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/patients/{patientId}/prescriptions")
    public ResponseEntity<PrescriptionResponse> addPrescription(@PathVariable Long patientId,
                                                                @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.addPrescription(patientId, request));
    }

    @Operation(summary = "Add diagnosis for a patient",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Diagnosis added", content = @Content(schema = @Schema(implementation = MedicalHistoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/patients/{patientId}/diagnosis")
    public ResponseEntity<MedicalHistoryResponse> addDiagnosis(@PathVariable Long patientId,
                                                               @Valid @RequestBody MedicalHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.addDiagnosis(patientId, request));
    }

    @Operation(summary = "Get patient medical history",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated medical histories")
            })
    @GetMapping("/patients/{patientId}/medical-history")
    public ResponseEntity<Page<MedicalHistoryResponse>> getPatientMedicalHistory(@PathVariable Long patientId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorService.getPatientMedicalHistory(patientId, PageRequest.of(page, size)));
    }
}


