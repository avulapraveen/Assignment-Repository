package com.hms.admin_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private String phone;
}
