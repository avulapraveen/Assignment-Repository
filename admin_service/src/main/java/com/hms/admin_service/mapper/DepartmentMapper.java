package com.hms.admin_service.mapper;

import com.hms.admin_service.dto.DepartmentRequest;
import com.hms.admin_service.dto.DepartmentResponse;
import com.hms.admin_service.enity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequest dto) {
        Department dept = new Department();
        dept.setName(dto.getName());
        dept.setDescription(dto.getDescription());
        return dept;
    }

    public DepartmentResponse toDto(Department department) {
        DepartmentResponse res = new DepartmentResponse();
        res.setId(department.getId());
        res.setName(department.getName());
        res.setDescription(department.getDescription());
        return res;
    }
}
