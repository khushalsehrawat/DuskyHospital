package com.example.DuskyHospital.service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DuskyHospital.entity.Appointment;
import com.example.DuskyHospital.entity.Doctor;
import com.example.DuskyHospital.entity.Patient;
import com.example.DuskyHospital.repository.AppointRepo;
import com.example.DuskyHospital.repository.DoctorRepo;
import com.example.DuskyHospital.repository.PatientRepo;

@Service
public class AppointmentService {

    @Autowired
    private AppointRepo appointRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    /**
     * Book an appointment between a patient and doctor
     */
    public Appointment bookAppointment(Long patientId, Long doctorId, Appointment appointment)
    {
        Optional<Patient> patientOptional=patientRepo.findById(patientId);
        Optional<Doctor> doctorOptional=doctorRepo.findById(doctorId);

        if (patientOptional.isPresent()&&doctorOptional.isPresent())
        {
            appointment.setPatient(patientOptional.get());
            appointment.setDoctor(doctorOptional.get());
            appointment.setStatus("BOOKED");
            return appointRepo.save(appointment);
        }else {
            throw new RuntimeException("Doctor or Patient Not Found");
        }
    }

    /**
     * Get all appointments booked by a patient
     */
    public List<Appointment> getAppointmentByPatient(Long patientId)
    {
        Optional<Patient> patientOptional=patientRepo.findById(patientId);
        if (patientOptional.isPresent()){
            return appointRepo.findByPatient(patientOptional.get());
        }else {
            return Collections.emptyList();
        }
    }

    /**
     * Get all appointments booked for a doctor
     */
    public List<Appointment> getAppointmentByDoctor(Long doctorId)
    {
        Optional<Doctor> doctorOptional=doctorRepo.findById(doctorId);
        if (doctorOptional.isPresent())
        {
            return appointRepo.findByDoctor(doctorOptional.get());
        }else {
            return Collections.emptyList();
        }
    }

    /**
     * Fetch single appointment by its ID
     */
    public Appointment getAppointmentById(Long id)
    {
        return appointRepo.findById(id).orElse(null);
    }

    public String cancelAppointment(Long id) {
        if (appointRepo.existsById(id)) {
            appointRepo.deleteById(id);
            return "Appointment deleted successfully.";
        } else {
            return "Appointment not found.";
        }
    }

    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        return appointRepo.findById(id).map(appointment -> {
            // Removed setting ID as it should not be changed
            appointment.setAppointDate(updatedAppointment.getAppointDate());
            return appointRepo.save(appointment);
        }).orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));
    }

}