# SkilVorae — Premium EdTech & Learning Management System

> A state-of-the-art, full-stack EdTech application built with **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA**, **Thymeleaf**, and a custom **Vanilla CSS Design System**.

---

## Pre-Configured Demo Credentials Access

The application comes pre-seeded with 3 user personas for testing:

| Persona | Email | Password | Role & Access Rights |
| :--- | :--- | :--- | :--- |
| **Student** | `student@skilvorae.com` | `password123` | My Courses, Course Player, Timed Quizzes, Verifiable Certificates, Student Dashboard, Wishlist |
| **Instructor** | `instructor@skilvorae.com` | `password123` | Instructor Dashboard, 4-Step Course Creation Wizard, Roster Management, CSV Data Exports |
| **Admin** | `admin@skilvorae.com` | `password123` | Admin Portal, User Security & Permissions, Certificate Authenticity Verifier, Audit Logs Export |

---

## Navigation Demo & Interactive Video Walkthrough

Watch the full application walkthrough, navigation, and interactive features in action:

<p align="center">
  <img src="assets/navigation.webp" alt="SkilVorae Interactive Navigation Demo" width="100%" style="border-radius: 12px; border: 1px solid #7C3AED;" />
</p>

### Live Demo Assets:
- **Interactive Animated Walkthrough**: [`assets/navigation.webp`](assets/navigation.webp)
- **Application Dashboard Screenshot**: [`assets/image.png`](assets/image.png)
- **Video Download**: [`assets/navigation.mp4`](assets/navigation.mp4)

### Step-by-Step Navigation Guide:
1. **Landing Page (`/`)**: Explore 3-cards-per-row featured courses, view top categories, or click **View More Courses →**.
2. **Course Catalog (`/courses`)**: Search by keyword/instructor, filter by category, difficulty level, min rating, and paginate through course listings.
3. **Student Learning Journey**:
   - **Authentication**: Log in at `/login` as `student@skilvorae.com` / `password123`.
   - **Enrolled Courses & Player**: View active progress on `/my-courses`, watch lessons on `/courses/{id}/learn`, toggle module completion.
   - **Quiz Engine**: Take timed assessments on `/assessments/{id}`, submit answers, and view instant score reports on `/assessments/result/{id}`.
   - **Certificates**: View and print official completion certificates on `/certificates/{id}`.
   - **Wishlist & Bookmarks**: Save favorite courses using `/api/wishlist/toggle/{courseId}`.
4. **Instructor Portal**:
   - **Authentication**: Log in at `/login` as `instructor@skilvorae.com` / `password123`.
   - **Dashboard**: Track enrollment metrics, student rosters, and earnings on `/instructor/dashboard`.
   - **Course Creation Wizard**: Launch the 4-step course authoring modal via `/instructor/dashboard?create=true`.
   - **CSV Export**: Download student enrollment rosters via `/api/exports/instructor/enrollments`.
5. **Admin Governance**:
   - **Authentication**: Log in at `/login` as `admin@skilvorae.com` / `password123`.
   - **User Security & Logs**: Inspect user permissions, search account rosters, review real-time audit logs, and verify certificate codes.
   - **CSV Export**: Download system security audit logs via `/api/exports/admin/audit-logs`.

---

## Core Application Features

### 1. Student Portal & Learning Experience
- **Structured 3-Cards Grid Catalog**: Clean 3-card-per-row grid layout on the home page with full search and pagination on `/courses`.
- **Interactive Checkout Modal**: Demo enrollment summary with price breakdown and 18% GST calculation.
- **Lesson Curriculum Player**: Dedicated lesson viewer with progress indicators, module breakdown, and "Mark as Complete" toggles.
- **Timed Quiz Engine**: Automated quiz assessments with countdown timers, question navigator buttons, and pass/fail thresholds.
- **Verifiable Certificates**: Printable completion certificates generated upon passing assessments.
- **Student Dashboard Analytics**: 6 dynamic stat cards, 7-day learning streak tracker, Chart.js activity charts, and milestone badges.

### 2. Instructor Portal
- **4-Step Course Creation Wizard**: Author new courses with title, description, category, difficulty, pricing, module breakdown, and lesson content.
- **Learner Roster & Analytics**: Track student enrollment dates, progress percentages, and course earnings.
- **CSV Data Export**: One-click downloadable CSV reports of student rosters and progress.

### 3. System Administration & Security
- **User Governance & Roles**: Manage student, instructor, and admin roles.
- **Certificate Verification Tool**: Publicly verify certificate authenticity using unique certificate serial numbers.
- **Real-Time Audit Logging**: Automatic logging of user logins, enrollment actions, assessment attempts, and admin modifications.
- **CSV Security Export**: Download system security audit logs in CSV format.

---

## Technology Stack

- **Backend**: Java 17 / 21, Spring Boot 3.2.4
- **Security**: Spring Security 6, JWT (JSON Web Tokens), BCrypt Password Encoder
- **Database & ORM**: H2 In-Memory Database (Oracle Compatibility Mode), Spring Data JPA, Hibernate
- **Frontend & Templating**: Thymeleaf Engine, Custom Vanilla CSS Design System, HTML5, JavaScript (ES6+)
- **Data Visualization**: Chart.js 4.4.1
- **Build Tool**: Apache Maven 3.9+
- **Static Cloud Hosting**: Netlify Ready (`public/` directory & `netlify.toml`)

---

## Running Locally

### Prerequisites
- JDK 17 or JDK 21 installed
- Apache Maven 3.6+ (or use the bundled Maven wrapper in `./maven/apache-maven-3.9.6`)

### Commands

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

## Cloud Hosting & Netlify Deployment Guide

### Option 1: Netlify Deployment
1. Connect your repository `Madhusmita-16/skilVorae` to [Netlify](https://netlify.com).
2. The project contains a pre-configured [`netlify.toml`](file:///f:/skilVorae/netlify.toml) file publishing from `public/`.
3. Netlify will deploy the site assets seamlessly.

### Option 2: Render.com (Recommended for Live Java Backend)
1. Sign up at [Render.com](https://render.com).
2. Create a new **Web Service** and connect `Madhusmita-16/skilVorae`.
3. Set **Runtime** to `Docker` (it uses the project's [`Dockerfile`](file:///f:/skilVorae/Dockerfile)).
4. Click **Create Web Service** to launch the live Java Spring Boot application.

---

## Project Structure

```
skilVorae/
├── assets/                        # Navigation video & preview images
├── public/                        # Netlify static build publishing folder
├── netlify.toml                   # Netlify deployment configuration
├── Dockerfile                     # Multi-stage Docker container build
├── src/
│   ├── main/
│   │   ├── java/com/skilvorae/
│   │   │   ├── controller/        # Web Controllers (Thymeleaf) & REST API Controllers
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── entity/            # JPA Entities (User, Course, Module, Wishlist, etc.)
│   │   │   ├── repository/        # Spring Data JPA Repositories
│   │   │   ├── security/          # Spring Security & JWT Filter
│   │   │   ├── service/           # Business Logic & Recommendation/Export Services
│   │   │   └── util/              # DataInitializer (Catalog Seeder)
│   │   └── resources/
│   │       ├── static/            # CSS, JS, and image assets
│   │       ├── templates/         # Thymeleaf HTML Templates
│   │       └── application.yml    # Application & H2 Configuration
└── pom.xml                        # Maven Build & Dependencies
```

---

## License

This project is released under the **MIT License**.
