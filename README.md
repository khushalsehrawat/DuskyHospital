# 🏥DuskyHospital - Hospital Appointment & Billing Management System

A complete **Spring Boot-based Hospital Appointment & Billing System** following **industry-grade architecture**, clean code practices, and robust **role-based access control**. This project simulates a real-world hospital workflow with appointment booking, billing, disease-specialized doctor assignment, PDF bill generation, and email notifications.

---

## 📌 Features

### 🧍 Patient Module
- Patient registration and login using JWT
- View own profile, appointments, and bills
- Secure access control (can’t update or access others' data)

### 👨‍⚕️ Doctor Module
- Doctor registration with specialization
- View own profile, appointments
- Assigned automatically to appointments based on disease

### 🏥 Admin Module
- Add/update/delete patients and doctors
- Create appointments manually if required
- Generate bills, view all data
- Access to all secured endpoints

### 🧾 Appointment & Disease Management
- Disease to doctor mapping via specialization
- Auto-matching doctor based on disease
- Secure endpoint access for patients & admin

### 💵 Billing System
- Admin generates bills after appointments
- Charges include:
  - Doctor Fee
  - Treatment Fee (default or custom per disease)
  - Medicine Charges
  - Basic Hospital Fee
  - Tax
- Bill auto-calculates all totals

### 📄 PDF Bill Generation
- PDF generated using iText
- Contains:
  - Hospital logo
  - Doctor & patient details
  - Itemized billing breakdown
  - Tax and total
  - Clean layout, custom font

### 📧 Email Integration
- After bill is generated:
  - PDF is emailed to patient’s email using JavaMailSender
  - Secure SMTP with TLS
  - Custom subject + attachment

---

## 🛠️ Tech Stack

| Layer       | Technology                      |
|-------------|----------------------------------|
| Backend     | Java 17, Spring Boot             |
| Database    | MySQL, Hibernate JPA             |
| Security    | Spring Security, JWT             |
| PDF         | iText 5.5.13                     |
| Mail        | JavaMailSender                   |
| Frontend    | HTML, Bootstrap, JavaScript (static) |
| Build Tool  | Maven                            |

---

## 📦 Project Structure

```
src
├── config/         # SecurityConfig, JWT Filter
├── controller/     # REST APIs
├── entity/         # JPA Entities: Patient, Doctor, Disease, Appointment, Bill
├── exception/      # Global exception handler
├── repository/     # Spring Data JPA Repos
├── service/        # Interfaces + Implementations
├── utils/          # PdfGenerator
└── static/         # HTML pages (Bootstrap form for patients)
```

---

## 🚦 API Endpoints

### 👤 Patient
| Method | Endpoint                  | Description                     |
|--------|---------------------------|---------------------------------|
| POST   | /api/patient/register     | Register a patient              |
| POST   | /api/patient/login        | Login with JWT                  |
| GET    | /api/patient              | View all patients (Admin only)  |
| GET    | /api/patient/{id}         | View one patient                |

### 👨‍⚕️ Doctor
| POST   | /api/doctor/register      | Add doctor with specialization  |
| GET    | /api/doctor/{id}          | Get doctor by ID                |
| GET    | /api/doctor/disease/{id}  | Doctors for a disease           |

### 🩺 Disease
| POST   | /api/disease              | Add disease                     |
| GET    | /api/disease              | Get all diseases                |

### 📅 Appointment
| POST   | /api/appointment/book     | Book an appointment             |
| GET    | /api/appointment/{id}     | Get one appointment             |
| DELETE | /api/appointment/{id}     | Cancel an appointment           |

### 💵 Billing
| GET    | /api/bill/{appointmentId} | View bill for appointment       |
| POST   | /api/bill/generate/{id}   | Admin generates bill manually   |
| POST   | /api/generate-pdf-and-email/{billId} | Generate bill + email PDF |

---

## 🔐 Security & Access Control

- **Spring Security** with **JWT**
- Users authenticated with:
  - Email & password
  - Token returned on login
- Role-based access using `@PreAuthorize`:
  - `ROLE_ADMIN` — full access
  - `ROLE_DOCTOR` — read-only own data
  - `ROLE_PATIENT` — view self only
- Token added to all requests:
  ```
  Authorization: Bearer <token>
  ```

---

## 🌐 Web Frontend (Optional)

### `index.html` Features
- Patient form to add name, age, gender, email, phone
- JavaScript fetch API to call backend
- View list of patients after submission
- Simple Bootstrap design

📁 File path: `/src/main/resources/static/index.html`

---

## 📄 PDF + Email: How It Works

1. Admin generates bill
2. `PdfGenerator` creates the PDF using iText
3. `EmailService` sends email to patient with PDF attached
4. Frontend/Swagger/postman confirms with `200 OK`

---

## 🚀 How to Run This Project

```bash
# 1. Clone the repo
git clone https://github.com/yourusername/dusky-hospital.git

# 2. Open in your IDE (IntelliJ, Eclipse)

# 3. Set MySQL DB in application.properties
spring.datasource.username=your_db_user
spring.datasource.password=your_db_pass

# 4. Add your email + app password
spring.mail.username=your_email
spring.mail.password=your_app_password

# 5. Run the app
mvn clean install
mvn spring-boot:run
```

Access API at:
```
http://localhost:8080
```

---

## 📫 Future Extensions

- React-based Admin Dashboard
- Patient profile page + QR Code
- Payment gateway integration
- Bill download history + database email logs

---

## 🧠 Best Practices Followed

- Clean separation of layers (Entity, Service, Controller)
- Secure endpoint exposure
- DTOs over raw entities (for frontend)
- No Lombok: Manual getters/setters for transparency
- `@JsonManagedReference` & `@JsonIgnore` to handle JSON loops
- Cascading relationships handled carefully
- Secure email via TLS
- Minimal frontend but functional

---

## 📁 Author

**Khushal Sehrawat**  
[LinkedIn](https://www.linkedin.com/in/khushal-sehrawat-850527279/) | `DuskyHospital` Spring Boot Project 2025

🌐 Visit my web development studio / Marketing Agency — [TheVB24.com](https://www.thevb24.com)



## Development & Maintenance

**Developed & Maintained by
[Khushal Sehrawat](https://github.com/khushalsehrawat)**
— Founder of
[TheBenzzCreations](https://www.thebenzzcreations.com/).

TheBenzzCreations is a digital development studio specializing in website development, custom software, mobile applications, e-commerce solutions and AI-powered business automation.

For development enquiries and collaborations, visit [TheBenzzCreations](https://www.thebenzzcreations.com/).

This repository contains the DuskyHospital Spring Boot REST API. It does not contain HTML pages or view templates, so website SEO metadata, structured data and visible footer credits belong in a web frontend. The original author and TheVB24 studio credits above are preserved.
