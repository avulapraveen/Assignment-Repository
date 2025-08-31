package com.hms.appointment_service.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AppointmentAuditAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @AfterReturning(pointcut = "execution(* com.hms.appointment_service.service.impl.AppointmentServiceImpl.*(..)) && " +
            "(execution(* bookAppointment(..)) || execution(* cancelAppointment(..)))", returning = "result")
    public void auditAppointmentChanges(JoinPoint jp, Object result) {
    }
}
