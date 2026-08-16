# SkilVorae — Premium EdTech & Learning Management System

> A state-of-the-art, full-stack EdTech application built with **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA**, **Thymeleaf**, and **Vanilla CSS Design System**.

---

## About The Project

**SkilVorae** is an enterprise-grade Learning Management System (LMS) designed for engineering, technology, and management professionals. It provides a complete end-to-end learning experience — from exploring a 40+ course catalog with real-time filters to interactive lesson streaming, quiz assessments, verifiable certificates, student dashboard analytics, instructor course creation wizards, and administrative platform governance.

## Navigation Demo & Interactive Walkthrough

Experience the end-to-end application workflow and navigation in action:

<p align="center">
  <video src="assets/navigation.mp4" width="100%" controls poster="assets/image.png">
    Your browser does not support HTML5 video playback. <a href="assets/navigation.mp4">Click here to watch the Navigation Demo MP4</a>.
  </video>
</p>

[![Navigation Demo Preview](assets/image.png)](assets/navigation.mp4)

### Direct Demo Video Downloads & Playback:
- **MP4 Format**: [Download Navigation Demo (MP4)](assets/navigation.mp4)
- **WebM Format**: [Download Navigation Demo (WebM)](assets/navigation.webm)

### Step-by-Step Navigation Guide:
1. **Landing Page (`/`)**: Explore 3-cards-per-row featured courses, navigate through header sections, or click **View More Courses →**.
2. **Course Catalog (`/courses`)**: Search by title/instructor, filter by category, difficulty level, min rating, and paginate through course listings.
3. **Student Learning Journey**:
   - **Login**: `student@skilvorae.com` / `password123`
   - **Enrolled Courses & Player**: View active progress on `/my-courses`, watch lessons on `/courses/{id}/learn`, toggle module completion.
   - **Quiz Engine**: Take timed assessments on `/assessments/{id}`, submit answers, and view instant score breakdown on `/assessments/result/{id}`.
   - **Certificates**: Generate and print official completion certificates on `/certificates/{id}`.
4. **Instructor Portal**:
   - **Login**: `instructor@skilvorae.com` / `password123`
   - **Dashboard**: Track enrollment metrics, student rosters, and earnings on `/instructor/dashboard`.
   - **Course Wizard**: Launch the 4-step course authoring modal via `/instructor/dashboard?create=true`.
5. **Admin Governance**:
   - **Login**: `admin@skilvorae.com` / `password123`
   - **User Security & Logs**: Inspect user permissions, search account rosters, review real-time audit logs, and verify certificate codes.

### Core Highlights & Design Principles
- **Clean & Professional UI**: Completely emoji-free presentation with clean, high-contrast typography, dark/light theme switching, and custom CSS design system.
- **100% Functional Buttons & Workflows**: All form submissions, course checkout modals, theme toggles, filters, quiz timers, profile edits, certificate downloads, and admin tools are fully implemented and functional.
- **Role-Based Security**: Comprehensive security matrix supporting **Student**, **Instructor**, and **Admin** personas with JWT cookie-based session management.
- **Rich 40+ Course Catalog**: Pre-seeded catalog covering Programming, Full Stack Development, Cloud & DevOps, Cybersecurity, Industrial Automation, Embedded Systems, Renewable Energy, Languages, and Management with INR pricing.

---

## Key Features & Capabilities

### 1. Student Portal & Learning Experience
- **Course Catalog & Filtering**: Search by title/instructor, filter by category, difficulty level, rating (4.5+ / 4.8+), and sort by price or popularity.
- **Interactive Checkout Modal**: Instant demo enrollment with price breakdown, 18% GST calculation, and instant course access.
- **Curriculum Player**: Dedicated lesson viewer with progress indicators, module breakdown, and "Mark as Complete" status toggles.
- **Quiz & Assessment Engine**: Automated timed assessments with pass/fail threshold calculation and immediate score report.
- **Verifiable Digital Certificates**: Printable certificates generated upon course completion, complete with unique verification codes.
- **Student Dashboard**: 6 dynamic stat cards, 7-day learning streak tracker, Chart.js learning activity visualizer, and achievement milestones.

### 2. Instructor Portal
- **4-Step Course Creation Wizard**: Create new courses with title, description, category, difficulty, pricing, module structure, and lesson content.
- **Student Roster Management**: Track learner progress, enrollment dates, assessment scores, and completion statuses.
- **Analytics & Metrics**: Monitor total course enrollments, average ratings, completion rates, and demo platform earnings.

### 3. System Administration Panel
- **User Governance**: View and manage all platform users (Students, Instructors, Admins).
- **Certificate Verification Tool**: Publicly verify certificate authenticity using unique certificate serial numbers.
- **System Audit Logs**: Real-time logging of user logins, enrollment actions, assessment attempts, and administrative modifications.
- **Platform Performance Metrics**: Revenue analytics, user growth charts, and category metrics.

---

## Pre-Configured Demo Credentials

The application comes seeded with pre-configured accounts for testing each user role:

| Persona | Email | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Student** | `student@skilvorae.com` | `password123` | My Courses, Course Player, Assessments, Certificates, Student Dashboard |
| **Instructor** | `instructor@skilvorae.com` | `password123` | Instructor Dashboard, Course Creation Wizard, Roster Analytics |
| **Admin** | `admin@skilvorae.com` | `password123` | Admin Portal, User Management, Certificate Verifier, System Audit Logs |

---

## Technology Stack

- **Backend**: Java 17 / 21, Spring Boot 3.2.4
- **Security**: Spring Security 6, JWT (JSON Web Tokens), BCrypt Hashing
- **Database & ORM**: H2 In-Memory Database (Oracle Compatibility Mode), Spring Data JPA, Hibernate
- **Frontend / Templating**: Thymeleaf Engine, Vanilla CSS Design System, HTML5, JavaScript (ES6+)
- **Data Visualization**: Chart.js 4.4.1
- **Build Tool**: Apache Maven 3.9+

---

## Running Locally

### Prerequisites
- JDK 17 or JDK 21 installed
- Apache Maven 3.6+ (or use bundled Maven in `./maven/apache-maven-3.9.6`)

### Execution Commands

```bash
# 1. Clone the repository
git clone https://github.com/Madhusmita-16/skilVorae.git
cd skilVorae

# 2. Compile and run using Maven
mvn spring-boot:run
```

Once started, open your browser and navigate to:
```
http://localhost:8080
```

---

## Project Structure

```
skilVorae/
├── assets/
├── src/
│   ├── main/
│   │   ├── java/com/skilvorae/
│   │   │   ├── controller/        # Web Controllers (Thymeleaf) & API Controllers
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── entity/            # JPA Entities (User, Course, Module, Lesson, etc.)
│   │   │   ├── repository/        # Spring Data JPA Repositories
│   │   │   ├── security/          # Spring Security & JWT Configuration
│   │   │   ├── service/           # Business Logic & Services
│   │   │   └── util/              # DataInitializer (Database Seeder)
│   │   └── resources/
│   │       ├── static/            # Custom CSS, JS (main.js, player.js), and images
│   │       ├── templates/         # Thymeleaf HTML Templates
│   │       └── application.yml    # Application & H2 Configuration
└── pom.xml                        # Maven Dependencies & Build Configuration
```

---

## Cloud Hosting & Deployment Guide

### Option 1: Render.com (Recommended for Java Spring Boot)
1. Sign up at [Render.com](https://render.com).
2. Create a new **Web Service** and connect your GitHub repository `Madhusmita-16/skilVorae`.
3. Set **Runtime** to `Docker` (it will use the project's [`Dockerfile`](file:///f:/skilVorae/Dockerfile)).
4. Click **Create Web Service**. Render will automatically compile and host the live Spring Boot application.

### Option 2: Netlify Deployment
1. Connect your repository `Madhusmita-16/skilVorae` to [Netlify](https://netlify.com).
2. The project contains a pre-configured [`netlify.toml`](file:///f:/skilVorae/netlify.toml) file:
   - **Publish directory**: `src/main/resources/static`
   - **API Proxy**: Redirects `/api/*` requests to your deployed Java backend service.
3. Click **Deploy Site**.

### Option 3: Railway.app (1-Click Deployment)
1. Import `Madhusmita-16/skilVorae` into [Railway.app](https://railway.app).
2. Railway detects Maven `pom.xml` automatically and executes `mvn clean package` and `java -jar target/*.jar`.

---

## License

This project is released under the **MIT License**.
