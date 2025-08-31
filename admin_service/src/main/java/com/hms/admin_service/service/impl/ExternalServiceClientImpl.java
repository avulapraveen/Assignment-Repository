package com.hms.admin_service.service.impl;

import com.hms.admin_service.dto.BillRequest;
import com.hms.admin_service.dto.BillResponse;
import com.hms.admin_service.dto.DoctorRequest;
import com.hms.admin_service.dto.DoctorResponse;
import com.hms.admin_service.dto.PatientRequest;
import com.hms.admin_service.dto.PatientResponse;
import com.hms.admin_service.service.ExternalServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ExternalServiceClientImpl implements ExternalServiceClient {

    private final WebClient webClient;

    @Override
    public Page<DoctorResponse> listDoctors(Pageable pageable) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("http://doctor-service/api/v1/doctors")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<DoctorResponse>>() {})
                .block();
    }

    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {
        return webClient.post()
                .uri("http://doctor-service/api/v1/doctors/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DoctorResponse.class)
                .block();
    }

    @Override
    public Page<PatientResponse> listPatients(Pageable pageable) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("http://patient-service/api/v1/patients")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<PatientResponse>>() {})
                .block();
    }

    @Override
    public PatientResponse createPatient(PatientRequest request) {
        return webClient.post()
                .uri("http://patient-service/api/v1/patients/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PatientResponse.class)
                .block();
    }

    @Override
    public Page<BillResponse> listBills(Pageable pageable) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("http://billing-service/api/v1/billing")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<BillResponse>>() {})
                .block();
    }

    @Override
    public BillResponse createBill(BillRequest request) {
        return webClient.post()
                .uri("http://billing-service/api/v1/billing/create")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BillResponse.class)
                .block();
    }
}
