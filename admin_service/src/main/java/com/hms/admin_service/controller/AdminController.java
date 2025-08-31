package com.hms.admin_service.controller;

import com.hms.admin_service.dto.*;
import com.hms.admin_service.service.DepartmentService;
import com.hms.admin_service.service.ExternalServiceClient;
import com.hms.admin_service.service.RoomAllocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Service", description = "Manage hospital departments, rooms, doctors, patients, and billing")
@Validated
public class AdminController {

    private final DepartmentService departmentService;
    private final RoomAllocationService roomAllocationService;
    private final ExternalServiceClient externalServiceClient;

    // Department APIs

    @Operation(summary = "Create a new department",
            description = "Create a new department in the hospital",
            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = DepartmentRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Department created successfully",
                            content = @Content(schema = @Schema(implementation = DepartmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(request));
    }

    @Operation(summary = "List all departments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of hospital departments",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DepartmentResponse.class))))
            })
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> listDepartments() {
        return ResponseEntity.ok(departmentService.listDepartments());
    }

    @Operation(summary = "Update an existing department",
            description = "Update details of an existing department",
            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = DepartmentRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Department updated successfully",
                            content = @Content(schema = @Schema(implementation = DepartmentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Department not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @Parameter(description = "ID of the department to update") @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @Operation(summary = "Delete a department",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Department not found")
            })
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @Parameter(description = "ID of the department to delete") @PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // Room allocation APIs

    @Operation(summary = "Allocate a room to a patient",
            description = "Create a room allocation record for a patient",
            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RoomAllocationRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Room allocated successfully",
                            content = @Content(schema = @Schema(implementation = RoomAllocationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/rooms/allocate")
    public ResponseEntity<RoomAllocationResponse> allocateRoom(
            @Valid @RequestBody RoomAllocationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomAllocationService.allocateRoom(request));
    }

    @Operation(summary = "Release a previously allocated room",
            description = "Mark a room allocation record as released",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Room released successfully"),
                    @ApiResponse(responseCode = "404", description = "Room allocation not found")
            })
    @PostMapping("/rooms/{id}/release")
    public ResponseEntity<Void> releaseRoom(
            @Parameter(description = "ID of the room allocation to release") @PathVariable Long id) {
        roomAllocationService.releaseRoom(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get room allocations for a patient",
            description = "Retrieve all room allocations for a specific patient",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of room allocations",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomAllocationResponse.class))))
            })
    @GetMapping("/rooms/patient/{patientId}")
    public ResponseEntity<List<RoomAllocationResponse>> getAllocationsForPatient(
            @Parameter(description = "Patient ID to retrieve room allocations for") @PathVariable Long patientId) {
        return ResponseEntity.ok(roomAllocationService.getAllocationsForPatient(patientId));
    }

    // Doctor management APIs (cross-service)

    @Operation(summary = "List doctors",
            description = "Retrieve paginated list of doctors from Doctor Service",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Page size", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of doctors",
                            content = @Content(schema = @Schema(implementation = Page.class)))
            })
    @GetMapping("/doctors")
    public ResponseEntity<Page<DoctorResponse>> listDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(externalServiceClient.listDoctors(PageRequest.of(page, size)));
    }

    @Operation(summary = "Create a doctor",
            description = "Create new doctor in Doctor Service",
            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = DoctorRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Doctor created successfully",
                            content = @Content(schema = @Schema(implementation = DoctorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(externalServiceClient.createDoctor(request));
    }

    // Patient management APIs (cross-service)

    @Operation(summary = "List patients",
            description = "Retrieve paginated list of patients from Patient Service",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Page size", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of patients",
                            content = @Content(schema = @Schema(implementation = Page.class)))
            })
    @GetMapping("/patients")
    public ResponseEntity<Page<PatientResponse>> listPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(externalServiceClient.listPatients(PageRequest.of(page, size)));
    }

    @Operation(summary = "Create a patient",
            description = "Create new patient in Patient Service",
            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = PatientRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Patient created successfully",
                            content = @Content(schema = @Schema(implementation = PatientResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/patients")
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(externalServiceClient.createPatient(request));
    }

    // Billing management APIs (cross-service)

    @Operation(summary = "List bills",
            description = "Retrieve paginated bills from Billing Service",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Page size", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of bills",
                            content = @Content(schema = @Schema(implementation = Page.class)))
            })
    @GetMapping("/billing")
    public ResponseEntity<Page<BillResponse>> listBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(externalServiceClient.listBills(PageRequest.of(page, size)));
    }

    @Operation(summary = "Create a bill",
            description = "Create new bill in Billing Service",
            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = BillRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Bill created successfully",
                            content = @Content(schema = @Schema(implementation = BillResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/billing")
    public ResponseEntity<BillResponse> createBill(
            @Valid @RequestBody BillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(externalServiceClient.createBill(request));
    }
}
