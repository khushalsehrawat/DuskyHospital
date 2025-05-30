package com.example.DuskyHospital.repository;

import com.example.DuskyHospital.entity.Appointment;
import com.example.DuskyHospital.entity.Doctor;
import com.example.DuskyHospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointRepo extends JpaRepository<Appointment,Long> {

    // Find appointments by patient
    List<Appointment> findByPatient(Patient patient);

    // Find appointments by doctor
    List<Appointment> findByDoctor(Doctor doctor);

    // Fetch all appointments with patient and doctor information
    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor")
    List<Appointment> findAllAppointmentsWithPatientAndDoctor();
}
