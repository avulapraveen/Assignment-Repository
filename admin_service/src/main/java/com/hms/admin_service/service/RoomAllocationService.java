package com.hms.admin_service.service;

import com.hms.admin_service.dto.RoomAllocationRequest;
import com.hms.admin_service.dto.RoomAllocationResponse;

import java.util.List;

public interface RoomAllocationService  {

    RoomAllocationResponse allocateRoom(RoomAllocationRequest request);
    void releaseRoom(Long allocationId);
    List<RoomAllocationResponse> getAllocationsForPatient(Long patientId);
}
