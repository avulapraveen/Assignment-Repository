package com.hms.patient_service.repository;

import com.hms.patient_service.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    Page<Prescription> findByPatientId(Long patientId, Pageable pageable);
}
