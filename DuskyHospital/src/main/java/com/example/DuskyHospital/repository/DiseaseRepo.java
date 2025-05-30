package com.example.DuskyHospital.repository;

import com.example.DuskyHospital.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseRepo extends JpaRepository<Disease,Long> {

    Optional<Disease> findByName(String name);

}
