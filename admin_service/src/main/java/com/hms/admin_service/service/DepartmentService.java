package com.hms.admin_service.service;

import com.hms.admin_service.dto.DepartmentRequest;
import com.hms.admin_service.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartment(Long id);
    List<DepartmentResponse> listDepartments();
}
