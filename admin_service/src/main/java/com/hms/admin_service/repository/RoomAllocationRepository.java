package com.hms.admin_service.repository;

import com.hms.admin_service.enity.RoomAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAllocationRepository extends JpaRepository<RoomAllocation, Long> {

    List<RoomAllocation> findByPatientId(Long patientId);
}
