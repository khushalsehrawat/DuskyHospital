package com.example.DuskyHospital.controller;


import com.example.DuskyHospital.entity.Patient;
import com.example.DuskyHospital.repository.PatientRepo;
import com.example.DuskyHospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // ✅ Only Admins can add a patient
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.registerPatient(patient);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatient()
    {
        return patientService.getAllPatient();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Patient getPatientById(@PathVariable Long id)
    {
        return patientService.getPatientById(id);
    }

    // 3. Create Patient - anyone can register (no authentication required)
    @PostMapping("/registerPatient")
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.registerPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // 4. Update Patient - allowed for ADMIN or USER (who owns it)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        return patientService.updatePatient(id, updatedPatient);
    }

    // 5. Delete Patient - only ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePatient(@PathVariable Long id) {
        return patientService.deletePatientById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatient());
    }

    // ✅ Optional: get a patient by email (for future use in security / profile settings)
    @GetMapping("/by-email/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.findByEmail(email);
    }


}
