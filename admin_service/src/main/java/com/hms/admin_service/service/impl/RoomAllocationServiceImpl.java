package com.hms.admin_service.service.impl;

import com.hms.admin_service.dto.RoomAllocationRequest;
import com.hms.admin_service.dto.RoomAllocationResponse;
import com.hms.admin_service.enity.RoomAllocation;
import com.hms.admin_service.mapper.RoomAllocationMapper;
import com.hms.admin_service.repository.RoomAllocationRepository;
import com.hms.admin_service.service.RoomAllocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomAllocationServiceImpl implements RoomAllocationService {

    private final RoomAllocationRepository repository;
    private final RoomAllocationMapper mapper;

    @Override
    public RoomAllocationResponse allocateRoom(RoomAllocationRequest request) {
        RoomAllocation allocation = mapper.toEntity(request);
        RoomAllocation saved = repository.save(allocation);
        return mapper.toDto(saved);
    }

    @Override
    public void releaseRoom(Long allocationId) {
        RoomAllocation allocation = repository.findById(allocationId).orElseThrow(() -> new RuntimeException("Room allocation not found"));
        allocation.setReleasedAt(LocalDateTime.now());
        repository.save(allocation);
    }

    @Override
    public List<RoomAllocationResponse> getAllocationsForPatient(Long patientId) {
        return repository.findByPatientId(patientId).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
