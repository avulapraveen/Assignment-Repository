package com.hms.admin_service.service.impl;

import com.hms.admin_service.dto.DepartmentRequest;
import com.hms.admin_service.dto.DepartmentResponse;
import com.hms.admin_service.enity.Department;
import com.hms.admin_service.mapper.DepartmentMapper;
import com.hms.admin_service.repository.DepartmentRepository;
import com.hms.admin_service.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper mapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department already exists");
        }
        Department dept = mapper.toEntity(request);
        Department saved = departmentRepository.save(dept);
        return mapper.toDto(saved);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department dept = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        dept.setName(request.getName());
        dept.setDescription(request.getDescription());
        return mapper.toDto(departmentRepository.save(dept));
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentResponse> listDepartments() {
        return departmentRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
