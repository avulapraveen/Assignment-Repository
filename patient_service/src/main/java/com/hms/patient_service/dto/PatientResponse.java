package com.hms.patient_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
