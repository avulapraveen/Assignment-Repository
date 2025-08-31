package com.hms.appointment_service.mapper;

import com.hms.appointment_service.dto.AppointmentRequest;
import com.hms.appointment_service.dto.AppointmentResponse;
import com.hms.appointment_service.model.Appointment;
import com.hms.appointment_service.model.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(appointmentRequest.getPatientId());
        appointment.setDoctorId(appointmentRequest.getDoctorId());
        appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointment;
    }

    public AppointmentResponse toDto(Appointment appointment) {
        AppointmentResponse resp = new AppointmentResponse();
        resp.setId(appointment.getId());
        resp.setPatientId(appointment.getPatientId());
        resp.setDoctorId(appointment.getDoctorId());
        resp.setAppointmentTime(appointment.getAppointmentTime());
        resp.setStatus(appointment.getStatus().name());
        return resp;
    }
}
