package com.example.DuskyHospital.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/duskyhospital")
public class TestController {


    @GetMapping
    public String publicAccess()
    {
        return "!!!WELCOME!!! to DuskyHospital";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('PATIENT')")
    public String patientAccess()
    {
        return "Helo!! , you can view your Appointment here";
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public String doctorAccess()
    {
        return "Hello Doctor!! MAnage your appointments and disease here";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess()
    {
        return "Here Manage all the things......";
    }

}
