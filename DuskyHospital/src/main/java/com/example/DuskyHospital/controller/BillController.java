package com.example.DuskyHospital.controller;

import com.example.DuskyHospital.entity.Bill;
import com.example.DuskyHospital.service.BillService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;


@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Generate Bill (ADMIN only)
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public Bill generateBill(
            @RequestParam Long appointmentId,
            @RequestParam double doctorFee,
            @RequestParam double treatmentFee,
            @RequestParam double medicineFee
    ) {
        return billService.generateBill(appointmentId, doctorFee, treatmentFee, medicineFee);
    }

    // ✅ Get Bill by Appointment ID (ADMIN only)
    @GetMapping("/getByAppointment/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Bill getBillByAppointmentId(@PathVariable Long appointmentId) {
        return billService.getBillByAppointmentId(appointmentId);
    }

    // ✅ Patients can view only their own bill
    @GetMapping("/view/patient/{appointmentId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Bill> viewPatientBill(@PathVariable Long appointmentId, Authentication authentication) {
        Bill bill = billService.getBillByAppointmentId(appointmentId);
        String loggedInEmail = authentication.getName();

        if (!bill.getAppointment().getPatient().getEmail().equals(loggedInEmail)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(bill);
    }

    // ✅ PDF generator with hospital logo, font, and page number
    private byte[] generateBillPdfWithDesign(Bill bill) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // Add page number
            writer.setPageEvent(new PdfPageEventHelper() {
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte cb = writer.getDirectContent();
                    Phrase footer = new Phrase("Page " + document.getPageNumber(), FontFactory.getFont(FontFactory.HELVETICA, 9));
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                            (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
                }
            });

            document.open();

            // Add logo
            InputStream logoStream = new ClassPathResource("static/hospital_logo.jpeg").getInputStream();
            Image logo = Image.getInstance(logoStream.readAllBytes());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_RIGHT);
            document.add(logo);

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Hospital Bill Summary", titleFont));
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

    // ✅ Download Bill PDF
    @GetMapping("/pdf/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    public ResponseEntity<byte[]> downloadBillPdf(@PathVariable Long appointmentId, Authentication authentication) {
        Bill bill = billService.getBillByAppointmentId(appointmentId);

        // Patient access control
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PATIENT"))) {
            String loggedInEmail = authentication.getName();
            if (!bill.getAppointment().getPatient().getEmail().equals(loggedInEmail)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        byte[] pdfData = generateBillPdfWithDesign(bill);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "bill_" + appointmentId + ".pdf");

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }

    // ✅ Send Bill via Email
    @PostMapping("/email/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendBillEmail(@PathVariable Long appointmentId) {
        Bill bill = billService.getBillByAppointmentId(appointmentId);
        byte[] pdfBytes = generateBillPdfWithDesign(bill);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(bill.getAppointment().getPatient().getEmail());
            helper.setSubject("Your Hospital Bill");
            helper.setText("Dear " + bill.getAppointment().getPatient().getName() + ",\n\nPlease find your hospital bill attached.\n\nThank you.");

            helper.addAttachment("hospital_bill.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
