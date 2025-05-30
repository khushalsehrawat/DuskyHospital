package com.example.DuskyHospital.entity;

import com.example.DuskyHospital.repository.BillRepo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    @JsonBackReference
    private Appointment appointment;

    private double DoctorFee;
    private double TreatmentFee;
    private double MedicineCharge;
    private double BasicCharge=500.0;
    private double Tax;
    private double TotalAmount;
    private Date date;
    private String treatment;

    public Bill(){}

    public Bill(Long id, Appointment appointment, double doctorFee, double treatmentFee, double medicineCharge, double basicCharge, double tax, double totalAmount) {
        this.id = id;
        this.appointment = appointment;
        DoctorFee = doctorFee;
        TreatmentFee = treatmentFee;
        MedicineCharge = medicineCharge;
        BasicCharge = basicCharge;
        Tax = tax;
        TotalAmount = totalAmount;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public double getDoctorFee() {
        return DoctorFee;
    }

    public void setDoctorFee(double doctorFee) {
        DoctorFee = doctorFee;
    }

    public double getTreatmentFee() {
        return TreatmentFee;
    }

    public void setTreatmentFee(double treatmentFee) {
        TreatmentFee = treatmentFee;
    }

    public double getMedicineCharge() {
        return MedicineCharge;
    }

    public void setMedicineCharge(double medicineCharge) {
        MedicineCharge = medicineCharge;
    }

    public double getBasicCharge() {
        return BasicCharge;
    }

    public void setBasicCharge(double basicCharge) {
        BasicCharge = basicCharge;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        Tax = tax;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public void setTreatment(String treatmentName) {
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getTreatment() {
        return treatment;
    }
}
