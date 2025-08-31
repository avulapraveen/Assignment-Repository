package com.hms.patient_service.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    private Long patientId;

    private Long doctorId;

    private String medication;

    private String dosage;

    private String instructions;

    private LocalDateTime issueDate;
}
