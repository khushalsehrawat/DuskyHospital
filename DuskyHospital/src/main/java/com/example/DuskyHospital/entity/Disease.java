package com.example.DuskyHospital.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private Double defaultCharge;

    @OneToMany(mappedBy = "disease", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    @ManyToMany(mappedBy = "diseases")
    @JsonBackReference
    private List<Doctor> doctors = new ArrayList<>();


    public Disease()
    {

    }

    public Disease(Long id, String name, String description, Double defaultCharge, List<Appointment> appointments, List<Doctor> doctors) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultCharge = defaultCharge;
        this.appointments = appointments;
        this.doctors = doctors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Double getDefaultCharge() {
        return defaultCharge;
    }

    public void setDefaultCharge(Double defaultCharge) {
        this.defaultCharge = defaultCharge;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
