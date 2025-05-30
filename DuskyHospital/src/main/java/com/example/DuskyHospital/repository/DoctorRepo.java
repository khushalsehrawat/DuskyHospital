package com.example.DuskyHospital.repository;

import com.example.DuskyHospital.entity.Disease;
import com.example.DuskyHospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepo extends JpaRepository<Doctor,Long> {

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findBySpecialization(String specialization);

    //Object findByDiseasesContaining(Disease disease);

    List<Doctor> findByDiseasesContaining(Disease disease);

}
