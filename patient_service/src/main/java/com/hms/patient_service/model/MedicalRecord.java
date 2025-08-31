package com.hms.patient_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Document(collection = "medical_records")
public class MedicalRecord {

    @Id
    private String id;

    private Long patientId;

    private String diagnosis;

    private String treatment;

    private LocalDateTime recordDate;

    private String notes;
}

