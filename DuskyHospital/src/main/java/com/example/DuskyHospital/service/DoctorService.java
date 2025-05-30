package com.example.DuskyHospital.service;


import com.example.DuskyHospital.entity.Disease;
import com.example.DuskyHospital.entity.Doctor;
import com.example.DuskyHospital.repository.DiseaseRepo;
import com.example.DuskyHospital.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DiseaseRepo diseaseRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    public Doctor registerDoctor(Doctor doctor)
    {
        return doctorRepo.save(doctor);
    }
    // Get doctor by ID
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepo.findById(id);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    // Get doctors by disease (fetch specialization match)
    public List<Doctor> getDoctorsByDisease(Long diseaseId) {
        Optional<Disease> disease = diseaseRepo.findById(diseaseId);
        return disease.map(doctorRepo::findByDiseasesContaining).orElseGet(java.util.Collections::emptyList);
    }

    // Assign a disease to doctor
    public String assignDiseaseToDoctor(Long doctorId, Long diseaseId) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        Optional<Disease> diseaseOpt = diseaseRepo.findById(diseaseId);

        if (doctorOpt.isPresent() && diseaseOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            Disease disease = diseaseOpt.get();
            doctor.getDiseases().add(disease);
            doctorRepo.save(doctor);
            return "Disease assigned successfully.";
        } else {
            return "Doctor or Disease not found.";
        }
    }

    public Doctor updateDoctor(Long id, Doctor doctor) {
        Optional<Doctor> existingDoctor = doctorRepo.findById(id);
        if (existingDoctor.isPresent()) {
            Doctor doc = existingDoctor.get();
            doc.setName(doctor.getName());
            doc.setSpecialization(doctor.getSpecialization());
            doc.setPhone(doctor.getPhone());
            return doctorRepo.save(doc);
        } else {
            throw new RuntimeException("Doctor not found");
        }

    }

    public String deleteDoctorById(Long id) {
        if (doctorRepo.existsById(id)) {
            doctorRepo.deleteById(id);
            return "Doctor deleted successfully.";
        } else {
            return "Doctor not found.";
        }
    }

}
