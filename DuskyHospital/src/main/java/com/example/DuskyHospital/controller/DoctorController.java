package com.example.DuskyHospital.controller;


import com.example.DuskyHospital.entity.Doctor;
import com.example.DuskyHospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor registerDoctor(@RequestBody Doctor doctor)
    {
        return doctorService.registerDoctor(doctor);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor)
    {
        return doctorService.updateDoctor(id,doctor);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN)")
    public String DeleteDoctorById(@PathVariable Long id)
    {
        return doctorService.deleteDoctorById(id);
    }

    // View one doctor — open to authenticated roles
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('USER')")
    public Optional<Doctor> getDoctor(@PathVariable Long id)
    {
        return doctorService.getDoctorById(id);
    }

    // Only ADMIN can view all doctors
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // ADMIN can assign disease to doctor
    @PostMapping("/{doctorId}/assignDisease/{diseaseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignDiseaseToDoctor(@PathVariable Long doctorId, @PathVariable Long diseaseId) {
        return doctorService.assignDiseaseToDoctor(doctorId, diseaseId);
    }

}
