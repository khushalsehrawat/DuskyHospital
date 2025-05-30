package com.example.DuskyHospital.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    @JsonBackReference
    private Disease disease;

    private LocalDate appointDate;


    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Bill bill;

    private String customTreatmentName;
    private Double customTreatmentCharge;

    private String status;

    public Appointment (){}

    public Appointment(Long id, Patient patient, Doctor doctor, Disease disease, LocalDate appointDate, Bill bill, String customTreatmentName, Double customTreatmentCharge, String status) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.disease = disease;
        this.appointDate = appointDate;
        this.bill = bill;
        this.customTreatmentName = customTreatmentName;
        this.customTreatmentCharge = customTreatmentCharge;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public LocalDate getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(LocalDate appointDate) {
        this.appointDate = appointDate;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void setStatus(String booked) {
        this.status = status;
    }

    public String getCustomTreatmentName() {
        return customTreatmentName;
    }

    public void setCustomTreatmentName(String customTreatmentName) {
        this.customTreatmentName = customTreatmentName;
    }

    public Double getCustomTreatmentCharge() {
        return customTreatmentCharge;
    }

    public void setCustomTreatmentCharge(Double customTreatmentCharge) {
        this.customTreatmentCharge = customTreatmentCharge;
    }

    public String getStatus() {
        return status;
    }
}
