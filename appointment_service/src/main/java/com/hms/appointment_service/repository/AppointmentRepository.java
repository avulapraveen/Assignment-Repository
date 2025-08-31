package com.hms.appointment_service.repository;

import com.hms.appointment_service.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);
}
