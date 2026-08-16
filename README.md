# SkilVorae — Online Learning & Career Skills Platform

> Full-stack Learning Management System built with **Java 17 / Spring Boot 3**, **MySQL**, **Spring Security 6 (JWT)**, **Spring Data JPA**, **Thymeleaf**, and **Vanilla CSS**.

---

## Demo Login Accounts

| Role | Email | Password |
| :--- | :--- | :--- |
| 🎓 **Student** | `student@skilvorae.com` | `password123` |
| 🧑‍🏫 **Instructor** | `instructor@skilvorae.com` | `password123` |
| 🛡️ **Admin** | `admin@skilvorae.com` | `password123` |

---

## Navigation Demo

> Click the preview to watch the full navigation clip.

[![SkilVorae Navigation Demo](assets/screenshot_home.png)](assets/navigation.webp)

---

## Implemented Features

### 🎓 Student
- 3-cards-per-row featured course grid on landing page
- Course catalog with keyword search, category, difficulty, rating filters, and pagination
- Course enrollment, video lesson player with module sidebar
- "Mark as Complete" progress tracking per lesson
- Timed quiz engine with auto countdown, question navigator, and score report
- Downloadable digital certificates with unique serial codes
- Course wishlist (bookmark / un-bookmark via API)
- Category-based course recommendation engine

### 🧑‍🏫 Instructor
- Instructor dashboard with learner count, completion rate, average rating, and earnings stats
- 4-step course creation wizard (title, category, difficulty, pricing, modules, lessons)
- Student roster view and CSV export (`/api/exports/instructor/enrollments`)

### 🛡️ Admin
- Admin dashboard with revenue chart, user growth, category distribution, and user list
- User role and account status management
- Certificate authenticity verifier by serial code
- Audit log viewer and CSV export (`/api/exports/admin/audit-logs`)

---

## Technology Stack

| Layer | Technology |
| :--- | :--- |
| Backend | Java 17, Spring Boot 3.2.4 |
| Database | MySQL 8.x, Spring Data JPA, Hibernate (`ddl-auto=update`) |
| Security | Spring Security 6, JWT Cookie (`SKILVORAE_JWT`), BCrypt |
| UI | Thymeleaf, Vanilla CSS, JavaScript ES6+ |
| Charts | Chart.js 4.4.1 |
| Build | Apache Maven 3.9+ |

---

## Setup

### Prerequisites
- Java 17+
- MySQL 8.x running on port `3306`

### 1. Create the database

```sql
CREATE DATABASE skilvoraedb;
```

### 2. Configure credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Run

```bash
git clone https://github.com/Madhusmita-16/skilVorae.git
cd skilVorae
mvn spring-boot:run
```

Open: `http://localhost:8080`

> Tables and seed data (40+ courses, demo users, enrollments) are created automatically on first run via Hibernate DDL and `DataInitializer.java`.

---

## License

MIT License
