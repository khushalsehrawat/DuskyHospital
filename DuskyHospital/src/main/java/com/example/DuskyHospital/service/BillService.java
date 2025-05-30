package com.example.DuskyHospital.service;


import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DuskyHospital.entity.Appointment;
import com.example.DuskyHospital.entity.Bill;
import com.example.DuskyHospital.repository.AppointRepo;
import com.example.DuskyHospital.repository.BillRepo;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;



@Service
public class BillService {

    @Autowired
    private AppointRepo appointRepo;

    @Autowired
    private BillRepo billRepo;

    public Bill generateBill(Long appointmentId, double doctorFee, double treatmentFee, double medicineFee)
    {
        Optional<Appointment> appointmentOptional=appointRepo.findById(appointmentId);

        if(appointmentOptional.isPresent())
        {
            Appointment appointment=appointmentOptional.get();

            Bill bill=new Bill();
            bill.setAppointment(appointment);
            bill.setDoctorFee(doctorFee);
            // Removed setting treatmentFee parameter as it is overwritten below
            bill.setMedicineCharge(medicineFee);
            bill.setBasicCharge(500.0);

            double treatmentCharge=0.0;
            String treatmentName="";
            if (appointment.getCustomTreatmentCharge() != null && appointment.getCustomTreatmentName() != null) {
                // Use custom
                treatmentCharge = appointment.getCustomTreatmentCharge();
                treatmentName = appointment.getCustomTreatmentName();
            } else if (appointment.getDisease() != null && appointment.getDisease().getDefaultCharge() != null) {
                // Use default from disease
                treatmentCharge = appointment.getDisease().getDefaultCharge();
                treatmentName = appointment.getDisease().getName();
            } else {
                throw new RuntimeException("No treatment charge available for this appointment.");
            }

            bill.setTreatment(treatmentName);
            bill.setTreatmentFee(treatmentCharge);

            double Total = doctorFee+treatmentCharge+medicineFee+ bill.getBasicCharge();
            double tax = Total * 0.18; //18 % GST
            bill.setTax(tax);
            bill.setTotalAmount(Total+tax);
            bill.setDate(new Date());

            return billRepo.save(bill);
        }else {
            throw new RuntimeException("Appointment Not Found");
        }
    }

    public Bill getBillByAppointmentId(Long appointmentId)
    {
        return billRepo.findByAppointmentId(appointmentId);
    }

    // Removed unused field treatmentCharge

    public byte[] generateBillPdf(Long appointmentId) {
        Bill bill = getBillByAppointmentId(appointmentId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("🏥 Hospital Bill Summary", titleFont));
            document.add(new Paragraph("Date: " + bill.getDate(), textFont));
            document.add(new Paragraph("Patient Name: " + bill.getAppointment().getPatient().getName(), textFont));
            document.add(new Paragraph("Doctor: " + bill.getAppointment().getDoctor().getName(), textFont));
            document.add(new Paragraph("Treatment: " + bill.getTreatment(), textFont));
            document.add(new Paragraph("Treatment Fee: ₹" + bill.getTreatmentFee(), textFont));
            document.add(new Paragraph("Doctor Fee: ₹" + bill.getDoctorFee(), textFont));
            document.add(new Paragraph("Medicine Charges: ₹" + bill.getMedicineCharge(), textFont));
            document.add(new Paragraph("Basic Charge: ₹" + bill.getBasicCharge(), textFont));
            document.add(new Paragraph("Tax (18%): ₹" + bill.getTax(), textFont));
            document.add(new Paragraph("Total Amount: ₹" + bill.getTotalAmount(), titleFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }


}
