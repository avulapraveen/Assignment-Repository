package com.hms.billing_service.aspect;

import com.hms.billing_service.dto.BillResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class BillingAuditAspect {

    private static final Logger logger = LoggerFactory.getLogger(BillingAuditAspect.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String AUDIT_TOPIC = "audit-events";

    @Around("execution(* com.hms.billing_service.service.impl.BillingServiceImpl.*(..)) && " +
            "(execution(* createBill(..)) || execution(* updatePaymentStatus(..)))")
    public Object auditBillingOperations(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();

        Long billId = null;
        String eventType = null;

        if (result instanceof BillResponse) {
            BillResponse bill = (BillResponse)result;
            billId = bill.getId();
            eventType = pjp.getSignature().getName().equals("createBill") ? "BillingCreated" : "BillingUpdated";
        }

        // Retrieve or set traceId in MDC (use org.slf4j.MDC or Log4j2 ThreadContext depending on logging lib)
        String traceId = org.slf4j.MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            org.slf4j.MDC.put("traceId", traceId);
        }

        if (billId != null) {
            String eventPayload = String.format(
                    "{\"eventType\":\"%s\",\"billId\":%d,\"traceId\":\"%s\"}",
                    eventType, billId, traceId);
            kafkaTemplate.send(AUDIT_TOPIC, eventPayload);
            logger.info("Published audit event: {}", eventPayload);
        }

        return result;
    }
}
