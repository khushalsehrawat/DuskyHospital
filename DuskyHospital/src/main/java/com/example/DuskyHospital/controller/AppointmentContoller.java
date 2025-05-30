package com.example.DuskyHospital.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DuskyHospital.entity.Appointment;
import com.example.DuskyHospital.repository.AppointRepo;
import com.example.DuskyHospital.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentContoller {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointRepo appointRepo;

    //book an appointment
    @PostMapping("/book")
    @PreAuthorize("hasRole('ADMIN')")
    public Appointment bookAppointment(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestBody Appointment appointment
    )
    {
        return appointmentService.bookAppointment(patientId, doctorId, appointment);
    }

    //view All Appointment
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<Appointment> getAppointmentByDoctor(@PathVariable Long doctorId)
    {
        return appointmentService.getAppointmentByDoctor(doctorId);
    }

    /**
     * 🔍 Get a single appointment by its ID [Admin]
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    /**
     * 🔍 View all appointments for a patient [Admin or Patient]
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public List<Appointment> getAppointmentsByPatient(@PathVariable Long patientId) {
        return appointmentService.getAppointmentByPatient(patientId);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody Appointment updatedAppointment) {
        return appointmentService.updateAppointment(id, updatedAppointment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelAppointment(@PathVariable Long id) {
        Appointment existing = appointRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));

        appointRepo.deleteById(id);
        return "Appointment canceled successfully.";
    }


}
