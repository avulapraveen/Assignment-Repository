package com.hms.admin_service.mapper;

import com.hms.admin_service.dto.RoomAllocationRequest;
import com.hms.admin_service.dto.RoomAllocationResponse;
import com.hms.admin_service.enity.RoomAllocation;
import org.springframework.stereotype.Component;

@Component
public class RoomAllocationMapper {

    public RoomAllocation toEntity(RoomAllocationRequest dto) {
        RoomAllocation ra = new RoomAllocation();
        ra.setPatientId(dto.getPatientId());
        ra.setRoomNumber(dto.getRoomNumber());
        ra.setAllocatedAt(dto.getAllocatedAt());
        return ra;
    }

    public RoomAllocationResponse toDto(RoomAllocation roomAllocation) {
        RoomAllocationResponse res = new RoomAllocationResponse();
        res.setId(roomAllocation.getId());
        res.setPatientId(roomAllocation.getPatientId());
        res.setRoomNumber(roomAllocation.getRoomNumber());
        res.setAllocatedAt(roomAllocation.getAllocatedAt());
        res.setReleasedAt(roomAllocation.getReleasedAt());
        return res;
    }
}
