package com.example.DuskyHospital.repository;

import com.example.DuskyHospital.entity.Doctor;
import com.example.DuskyHospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient,Long> {

    Optional<Patient> findByEmail(String email);
}
