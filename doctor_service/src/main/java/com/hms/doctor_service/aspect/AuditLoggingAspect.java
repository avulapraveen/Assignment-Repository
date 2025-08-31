package com.hms.doctor_service.aspect;

import com.hms.doctor_service.model.Doctor;
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

    @AfterReturning(pointcut = "execution(* com.hms.doctor_service.service.impl.DoctorServiceImpl.*(..)) && " +
            "(execution(* register(..)) || execution(* updateProfile(..)))", returning = "result")
    public void auditDoctorChanges(JoinPoint joinPoint, Object result) {
        if (result instanceof Doctor doctor) {
            String event = String.format("{\"auditEvent\":\"DoctorDataChanged\", \"doctorId\":%d}", doctor.getId());
            kafkaTemplate.send("audit-events", event);
        }
    }
}
