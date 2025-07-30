# 💑 Matrimony Web Application (Spring Boot)

A full-featured Matrimony application built with Spring Boot, JWT security, and MySQL. Users can register, manage profiles, upload biodata and images, and search for suitable matches based on preferences. Includes admin panel for monitoring and management.

---

## 🚀 Features

- ✅ User registration and login (JWT based)
- 🔐 Role-based authentication (User/Admin)
- 📸 Upload profile photos & biodata (PDF)
- 🔍 Search profiles by age, religion, education, etc.
- 💌 Express interest in other profiles
- 📧 Email notifications (registration, password reset)
- 🔄 Forgot password with secure token
- 📊 Admin dashboard for profile management

---

## 🧰 Tech Stack

- **Backend:** Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate
- **Database:** MySQL
- **Authentication:** JWT (Token-based)
- **Mail Service:** JavaMailSender
- **File Upload:** Multipart (stored as BLOB or filesystem)

---

## 📁 Project Structure

src/
├── main/
│ ├── java/
│ │ └── com/example/matrimony/
│ │ ├── controller/
│ │ ├── service/
│ │ ├── repository/
│ │ ├── model/
│ │ └── config/
│ └── resources/
│ ├── application.properties
│ └── templates/
└── test/

--------------------------------------------------------------------


📬 API Endpoints

Method	Endpoint	                          Description

POST	  /api/users/register	              Register new user
POST	  /api/users/login	                Login and get JWT token
GET	    /api/profiles/match	              Search matches
POST	  /api/profiles/photo	              Upload profile photo
POST	  /api/users/forgot-password	      Request password reset

---------------------------------------------------------------------

🧑‍💻 Author
Name: Adinath Ingole

LinkedIn: https://www.linkedin.com/in/adinath-ingole/

GitHub: @AdinathIngole
