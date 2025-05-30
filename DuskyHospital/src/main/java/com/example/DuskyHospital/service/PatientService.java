package com.example.DuskyHospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DuskyHospital.entity.Patient;
import com.example.DuskyHospital.repository.PatientRepo;


@Service
public class PatientService {

    @Autowired
    private PatientRepo patientRepo;

    public Patient registerPatient(Patient patient)
    {
        try {
            return patientRepo.save(patient);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed To Save Patient");
            return null;
        }
    }

    public List<Patient> getAllPatient()
    {
        return patientRepo.findAll();
    }

    public Patient getPatientById(Long id)
    {
        return patientRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Patient Not Found with is ID : " + id));
    }

    public Patient findByEmail(String email)
    {
        return patientRepo.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Patient Not Found with email : " + email));
    }

    public String deletePatientById(Long id)
    {
        Patient patient=getPatientById(id);
        patientRepo.deleteById(id);
        return "Patient Deleted";
    }

    public Patient updatePatient(Long id, Patient updatedPatient)
    {
        Patient existing=getPatientById(id);
        existing.setName(updatedPatient.getName());
        existing.setEmail(updatedPatient.getEmail());
        existing.setPassword(updatedPatient.getPassword());
        return patientRepo.save(existing);
    }


}

