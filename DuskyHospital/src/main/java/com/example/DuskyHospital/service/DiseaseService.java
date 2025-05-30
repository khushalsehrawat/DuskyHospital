package com.example.DuskyHospital.service;


import com.example.DuskyHospital.entity.Disease;
import com.example.DuskyHospital.entity.Patient;
import com.example.DuskyHospital.repository.DiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseService {

    @Autowired
    private DiseaseRepo diseaseRepo;

    public Disease addDisease(Disease disease)
    {
        return diseaseRepo.save(disease);
    }

    public List<Disease> getAllDisease()
    {
        return diseaseRepo.findAll();
    }

    public Disease getDiseaseById(Long id)
    {
        return diseaseRepo.findById(id).orElse(null);
    }

    // Delete disease
    public String deleteDisease(Long id) {
        if (diseaseRepo.existsById(id)) {
            diseaseRepo.deleteById(id);
            return "Disease deleted successfully";
        } else {
            return "Disease not found";
        }
    }

    // Update existing disease
    public Disease updateDisease(Long id, Disease updatedDisease) {
        return diseaseRepo.findById(id).map(disease -> {
            disease.setName(updatedDisease.getName());
            disease.setDescription(updatedDisease.getDescription());
            return diseaseRepo.save(disease);
        }).orElseThrow(() -> new RuntimeException("Disease not found with ID: " + id));
    }

}

