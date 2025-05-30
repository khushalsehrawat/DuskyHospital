package com.example.DuskyHospital.controller;


import com.example.DuskyHospital.entity.Disease;
import com.example.DuskyHospital.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disease")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    // Only admin can register a new disease
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public Disease registerDisease(@RequestBody Disease disease)
    {
        return diseaseService.addDisease(disease);
    }

    @GetMapping
    public List<Disease> getAllDisease()
    {return diseaseService.getAllDisease();}

    @GetMapping("/{id}")
    public Disease getDiseaseById(@PathVariable Long id)
    {
        return diseaseService.getDiseaseById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Disease updateDisease(@PathVariable Long id, @RequestBody Disease disease) {
        return diseaseService.updateDisease(id, disease);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDisease(@PathVariable Long id) {
        return diseaseService.deleteDisease(id);
    }


}
