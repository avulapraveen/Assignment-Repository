package com.hms.patient_service.controller;

import com.hms.patient_service.dto.MedicalRecordRequest;
import com.hms.patient_service.dto.MedicalRecordResponse;
import com.hms.patient_service.dto.PatientRequest;
import com.hms.patient_service.dto.PatientResponse;
import com.hms.patient_service.dto.PrescriptionRequest;
import com.hms.patient_service.dto.PrescriptionResponse;
import com.hms.patient_service.mapper.PatientMapper;
import com.hms.patient_service.model.MedicalRecord;
import com.hms.patient_service.model.Patient;
import com.hms.patient_service.model.Prescription;
import com.hms.patient_service.service.MedicalRecordService;
import com.hms.patient_service.service.PatientService;
import com.hms.patient_service.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Service", description = "Manage patients, prescriptions, and medical records")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final PrescriptionService prescriptionService;
    private final MedicalRecordService medicalRecordService;

    @Operation(summary = "Register new patient",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Patient registered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(@Valid @RequestBody PatientRequest request) {
        PatientResponse created = patientService.registerPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get patient by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Patient not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List patients with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of patients")
            })
    @GetMapping
    public ResponseEntity<Page<PatientResponse>> listPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Patient> patients = patientService.listPatients(PageRequest.of(page, size));
        return ResponseEntity.ok(patients.map(patientMapper::toResponse));
    }

    @Operation(summary = "Update patient profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Patient not found"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updateProfile(@PathVariable Long id,
                                                         @Valid @RequestBody PatientRequest request) {
        Patient updated = patientService.updateProfile(id, request);
        return ResponseEntity.ok(patientMapper.toResponse(updated));
    }

    @Operation(summary = "Create new prescription for patient",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Prescription created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    @PostMapping("/{patientId}/prescriptions")
    public ResponseEntity<Prescription> addPrescription(@PathVariable Long patientId,
                                                        @Valid @RequestBody PrescriptionRequest request) {
        Prescription created = prescriptionService.createPrescription(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get prescriptions for patient with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of prescriptions")
            })
    @GetMapping("/{patientId}/prescriptions")
    public ResponseEntity<Page<Prescription>> getPrescriptions(@PathVariable Long patientId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(prescriptionService.getPrescriptions(patientId, PageRequest.of(page, size)));
    }

    @Operation(summary = "Add medical record record for patient",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Medical record record created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalRecordResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    @PostMapping("/{patientId}/medical-record")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@PathVariable Long patientId,
                                                           @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .patientId(patientId)
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .notes(request.getNotes())
                .recordDate(LocalDateTime.now())
                .build();

        MedicalRecord created = medicalRecordService.createMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get medical record for patient with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of medical record records")
            })
    @GetMapping("/{patientId}/medical-record")
    public ResponseEntity<Page<MedicalRecord>> getMedicalRecord(@PathVariable Long patientId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecord(patientId, PageRequest.of(page, size)));
    }
}