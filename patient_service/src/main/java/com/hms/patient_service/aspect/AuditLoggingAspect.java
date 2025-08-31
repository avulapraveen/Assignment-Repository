package com.hms.patient_service.aspect;

import com.hms.patient_service.model.MedicalRecord;
import com.hms.patient_service.model.Patient;
import com.hms.patient_service.model.Prescription;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @AfterReturning(pointcut = "execution(* com.hms.patient_service.service.impl.PatientServiceImpl*(..)) && " +
            "(execution(* register(..)) || execution(* updateProfile(..)))", returning = "result")
    public void auditPatientChanges(JoinPoint joinPoint, Object result) {
        if (result instanceof Patient patient) {
            String event = String.format("{\"auditEvent\":\"PatientDataChanged\", \"patientId\":%d}", patient.getId());
            kafkaTemplate.send("audit-events", event);
        }
    }

    @AfterReturning(pointcut = "execution(* com.hms.patient_service.service.impl.PrescriptionServiceImpl.createPrescription(..)) " +
            "&& args(prescription)", returning = "result")
    public void auditPrescriptionCreate(JoinPoint jp, Prescription prescription, Object result) {
        String event = String.format("{\"auditEvent\":\"PrescriptionCreated\", \"patientId\":%d}", prescription.getPatientId());
        kafkaTemplate.send("audit-events", event);
    }

    @AfterReturning(pointcut = "execution(* com.hms.patient_service.service.impl.MedicalRecordServiceImpl.createMedicalHistory(..)) " +
            "&& args(medicalRecord)", returning = "result")
    public void auditMedicalRecordCreate(JoinPoint jp, MedicalRecord medicalRecord, Object result) {
        String event = String.format("{\"auditEvent\":\"MedicalRecordCreated\", \"patientId\":%d}", medicalRecord.getPatientId());
        kafkaTemplate.send("audit-events", event);
    }
}
