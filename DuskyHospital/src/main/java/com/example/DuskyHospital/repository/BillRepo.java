package com.example.DuskyHospital.repository;

import com.example.DuskyHospital.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepo extends JpaRepository<Bill, Long> {

    // Fetch bills with appointments
    @Query("SELECT b FROM Bill b JOIN FETCH b.appointment")
    List<Bill> findAllBillsWithAppointments();

    Bill findByAppointmentId(Long appointmentId);
}
