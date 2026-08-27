package com.skilvorae.util;

import com.skilvorae.entity.*;
import com.skilvorae.entity.Module;
import com.skilvorae.enums.Difficulty;
import com.skilvorae.enums.EnrollmentStatus;
import com.skilvorae.enums.Role;
import com.skilvorae.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserProgressRepository userProgressRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final InstructorEarningsRepository instructorEarningsRepository;
    private final CourseQARepository courseQARepository;
    private final CourseBatchRepository courseBatchRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initialization.");
            return;
        }

        log.info("Seeding SkilVorae 40+ course catalog with INR pricing & demo users...");

        // 1. Seed Users (Student, Instructor, Admin)
        User student = User.builder()
                .fullName("Alex Morgan")
                .email("student@skilvorae.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.STUDENT)
                .phone("+1 (555) 234-5678")
                .qualification("Bachelor's Degree")
                .areaOfInterest("Software Engineering")
                .build();
        userRepository.save(student);

        User instructor = User.builder()
                .fullName("Prof. Sarah Jenkins")
                .email("instructor@skilvorae.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.INSTRUCTOR)
                .phone("+1 (555) 876-5432")
                .expertise("Full Stack Web Development, Java 17 & Spring Boot")
                .yearsOfExperience(12)
                .bio("Senior Software Architect and EdTech Instructor with 12+ years of enterprise application engineering experience.")
                .build();
        userRepository.save(instructor);

        User admin = User.builder()
                .fullName("System Administrator")
                .email("admin@skilvorae.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.ADMIN)
                .phone("+1 (555) 999-0000")
                .build();
        userRepository.save(admin);

        // 2. Seed Categories
        Category catProg = Category.builder().name("Programming").slug("programming").description("Core Java, Python, C, C++, C#, JS, TS, Kotlin, Go, PHP, Rust.").icon("code").build();
        Category catFullStack = Category.builder().name("Full Stack Development").slug("full-stack").description("Java Full Stack, MERN, MEAN, .NET, Spring Boot & Microservices.").icon("layers").build();
        Category catWeb = Category.builder().name("Web Development").slug("web-dev").description("HTML, CSS, JavaScript, React.js, Angular, Vue.js, Node.js, REST APIs.").icon("globe").build();
        Category catDb = Category.builder().name("Data & Database").slug("data-db").description("SQL, MySQL, Oracle SQL & PL/SQL, PostgreSQL, MongoDB, Data Analytics.").icon("database").build();
        Category catCloud = Category.builder().name("Cloud & DevOps").slug("cloud-devops").description("AWS, Azure, GCP, Docker, Kubernetes, DevOps, CI/CD, Terraform.").icon("cloud").build();
        Category catSecurity = Category.builder().name("Cybersecurity").slug("cybersecurity").description("Ethical Hacking, Network Security, Pen Testing, Web App Security.").icon("shield").build();
        Category catIT = Category.builder().name("IT & Infrastructure").slug("it-infrastructure").description("Computer Networks, Operating Systems, Linux, Linux Admin, IT Support.").icon("cpu").build();
        Category catMgmt = Category.builder().name("Management").slug("management").description("Project Mgmt, Product Mgmt, Business Analysis, Agile & Scrum, Marketing.").icon("briefcase").build();
        Category catAuto = Category.builder().name("Automation & Hardware").slug("automation-hardware").description("PLC, SCADA, Robotics, Embedded Systems, IoT, BMS, PCB Design, 3D Printing.").icon("cpu").build();
        Category catEmerging = Category.builder().name("Emerging Technologies").slug("emerging-tech").description("Industry 4.0, AI, Machine Learning, Quantum Computing, RHEL, CCNA, CCNP, CEH.").icon("zap").build();
        Category catRenewable = Category.builder().name("Renewable Energy").slug("renewable-energy").description("Solar PV Installation, Electric Vehicles, ETAP Power Analysis.").icon("sun").build();
        Category catLang = Category.builder().name("Languages & Soft Skills").slug("languages-career").description("English, French, Spanish, German, Japanese, Korean, Career Skills, Digital Marketing.").icon("globe").build();

        categoryRepository.saveAll(List.of(catProg, catFullStack, catWeb, catDb, catCloud, catSecurity, catIT, catMgmt, catAuto, catEmerging, catRenewable, catLang));

        // 3. Build Catalog
        createCourseCatalog(catProg, catFullStack, catWeb, catDb, catCloud, catSecurity, catIT, catMgmt, catAuto, catEmerging, catRenewable, catLang, student, instructor);

        // Seed Notifications
        Notification n1 = Notification.builder().user(student).title("Welcome to SkilVorae!").message("Explore our catalog of 40+ trending tech & management courses.").type("SYSTEM").build();
        Notification n2 = Notification.builder().user(student).title("Java 17 Masterclass Progress").message("You completed 2 lessons in Java 17 Enterprise Masterclass! Keep going.").type("ENROLLMENT").build();
        notificationRepository.saveAll(List.of(n1, n2));

        log.info("SkilVorae 40+ course catalog seeded successfully!");
    }

    private void createCourseCatalog(Category catProg, Category catFullStack, Category catWeb, Category catDb, Category catCloud, Category catSecurity, Category catIT, Category catMgmt, Category catAuto, Category catEmerging, Category catRenewable, Category catLang, User student, User instructor) {
        // --- PROGRAMMING (11 Courses) ---
        Course c1 = addCourse("Java 17 Enterprise Masterclass", "java-17-enterprise-masterclass", "Master Java 17 LTS, Object-Oriented Design, Collections, Streams, and Multithreading.", "Prof. Sarah Jenkins", catProg, Difficulty.BEGINNER, 14.5, "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=600&q=80", 4.9, 1499.0, 2999.0, 50, 1240);
        Course c2 = addCourse("Python 3 Programming: Beginner to Advanced", "python-3-programming-mastery", "Learn Python 3 syntaxes, data structures, OOP, file handling, and scripts.", "Elena Rostova", catProg, Difficulty.BEGINNER, 16.0, "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=600&q=80", 4.85, 1299.0, 2599.0, 50, 1560);
        addCourse("C Programming: System Low-Level & Memory", "c-programming-system-memory", "Pointers, memory allocation, data structures, and low-level system development.", "Dr. Michael Vance", catProg, Difficulty.BEGINNER, 12.0, "https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=600&q=80", 4.75, 999.0, 1999.0, 50, 890);
        addCourse("C++ Modern Systems & Game Engineering", "cpp-modern-systems-game", "C++17/C++20, STL, templates, memory management, and OOP architecture.", "Dr. Michael Vance", catProg, Difficulty.INTERMEDIATE, 18.0, "https://images.unsplash.com/photo-1509228468518-180dd4864904?auto=format&fit=crop&w=600&q=80", 4.88, 1499.0, 2999.0, 50, 1100);
        addCourse("C# & .NET 8 Enterprise Applications", "csharp-dotnet-8-enterprise", "Build enterprise APIs and desktop tools using C# 12 and .NET 8 framework.", "David Miller", catProg, Difficulty.INTERMEDIATE, 15.0, "https://images.unsplash.com/photo-1516259762381-22954d7d3ad2?auto=format&fit=crop&w=600&q=80", 4.8, 1399.0, 2799.0, 50, 950);
        addCourse("JavaScript ES6+ Deep Dive & Asynchronous Patterns", "javascript-es6-async-deep-dive", "Closures, promises, async/await, DOM, modules, and event loop mechanics.", "Alex Rivera", catProg, Difficulty.BEGINNER, 13.5, "https://images.unsplash.com/photo-1579468118864-1b9ea3c0db4a?auto=format&fit=crop&w=600&q=80", 4.92, 1199.0, 2399.0, 50, 1820);
        addCourse("TypeScript Professional Development", "typescript-professional-dev", "Static typing, generics, interfaces, decorators, and enterprise TS tooling.", "Alex Rivera", catProg, Difficulty.INTERMEDIATE, 11.0, "https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?auto=format&fit=crop&w=600&q=80", 4.87, 1299.0, 2599.0, 50, 1340);
        addCourse("Kotlin for Java Developers", "kotlin-java-developers", "Kotlin syntax, null safety, coroutines, and Android/backend migration.", "Prof. Sarah Jenkins", catProg, Difficulty.INTERMEDIATE, 10.5, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.82, 1199.0, 2399.0, 50, 720);
        addCourse("Go (Golang) Microservices & Concurrency", "go-golang-microservices-concurrency", "Goroutines, channels, interfaces, high-performance web servers, and gRPC.", "David Miller", catProg, Difficulty.ADVANCED, 14.0, "https://images.unsplash.com/photo-1607799279861-4dd421887fb3?auto=format&fit=crop&w=600&q=80", 4.91, 1599.0, 3199.0, 50, 980);
        addCourse("PHP 8 Modern Backend Architecture", "php-8-modern-backend", "PHP 8 OOP, Composer, Laravel framework, REST APIs, and MySQL integration.", "Elena Rostova", catProg, Difficulty.BEGINNER, 12.5, "https://images.unsplash.com/photo-1599507593499-a3f7d7d97667?auto=format&fit=crop&w=600&q=80", 4.72, 999.0, 1999.0, 50, 650);
        addCourse("Rust Systems Programming & Memory Safety", "rust-systems-programming-memory", "Ownership, borrowing, lifetimes, cargo, async Rust, and low-level safety.", "Marcus Thorne", catProg, Difficulty.ADVANCED, 16.5, "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=600&q=80", 4.95, 1799.0, 3599.0, 50, 840);

        // --- FULL STACK DEVELOPMENT (8 Courses) ---
        Course cFS = addCourse("Java Full Stack Development: Spring Boot 3 & Microservices", "java-full-stack-spring-boot-microservices", "Spring Boot 3, Spring Security 6, JWT, Thymeleaf, JPA, and Oracle DB.", "Prof. Sarah Jenkins", catFullStack, Difficulty.ADVANCED, 22.0, "https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=600&q=80", 4.95, 1999.0, 3999.0, 50, 1890);
        addCourse("Python Full Stack Development with Django & React", "python-full-stack-django-react", "Build scalable web apps using Django REST framework, React UI, and PostgreSQL.", "Elena Rostova", catFullStack, Difficulty.ADVANCED, 20.0, "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=600&q=80", 4.89, 1899.0, 3799.0, 50, 1420);
        addCourse("MERN Stack Development: MongoDB, Express, React & Node", "mern-stack-development", "Full stack JavaScript with MongoDB, Express server, React components, and Node.", "Alex Rivera", catFullStack, Difficulty.ADVANCED, 20.0, "https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80", 4.92, 1799.0, 3599.0, 50, 1650);
        addCourse("MEAN Stack Development: MongoDB, Express, Angular & Node", "mean-stack-development", "Enterprise full stack TypeScript with Angular framework, Express, and MongoDB.", "Alex Rivera", catFullStack, Difficulty.ADVANCED, 19.5, "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?auto=format&fit=crop&w=600&q=80", 4.81, 1699.0, 3399.0, 50, 910);
        addCourse(".NET Full Stack Development with C# & React", "dotnet-full-stack-csharp-react", "ASP.NET Core Web API, Entity Framework Core, React, and SQL Server.", "David Miller", catFullStack, Difficulty.ADVANCED, 21.0, "https://images.unsplash.com/photo-1516259762381-22954d7d3ad2?auto=format&fit=crop&w=600&q=80", 4.86, 1899.0, 3799.0, 50, 830);
        addCourse("Spring Boot 3 & Cloud Microservices Architecture", "spring-boot-cloud-microservices", "Spring Cloud Eureka, API Gateway, Resilience4j, OpenFeign, and Distributed Tracing.", "Prof. Sarah Jenkins", catFullStack, Difficulty.ADVANCED, 18.5, "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=600&q=80", 4.96, 1999.0, 3999.0, 50, 1750);
        addCourse("React.js & Node.js Masterclass", "reactjs-nodejs-masterclass", "Build full stack JavaScript web applications with modern state and API engine.", "Alex Rivera", catFullStack, Difficulty.INTERMEDIATE, 16.0, "https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80", 4.88, 1599.0, 3199.0, 50, 1290);
        addCourse("Python Django Web Development", "python-django-web-dev", "Django ORM, views, templates, authentication, and REST framework.", "Elena Rostova", catFullStack, Difficulty.INTERMEDIATE, 15.0, "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=600&q=80", 4.83, 1499.0, 2999.0, 50, 1050);

        // --- WEB DEVELOPMENT (7 Courses) ---
        addCourse("HTML & CSS: Responsive Web Design", "html-css-responsive-web-design", "HTML5 semantics, CSS flexbox, grid, animations, and mobile-first layouts.", "Alex Rivera", catWeb, Difficulty.BEGINNER, 10.0, "https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=600&q=80", 4.8, 999.0, 1999.0, 50, 1420);
        addCourse("JavaScript & React.js Frontend Masterclass", "javascript-react-frontend-masterclass", "Modern JS ES6+, React Hooks, Context API, React Router, and Redux Toolkit.", "Alex Rivera", catWeb, Difficulty.BEGINNER, 15.0, "https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80", 4.9, 1499.0, 2999.0, 50, 1610);
        addCourse("React.js 18 Deep Dive", "reactjs-18-deep-dive", "Concurrent rendering, Server Components, Custom Hooks, and state management.", "Alex Rivera", catWeb, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80", 4.91, 1399.0, 2799.0, 50, 1200);
        addCourse("Angular Enterprise Architecture", "angular-enterprise-architecture", "TypeScript, RxJS, NgRx, Dependency Injection, and Angular CLI.", "David Miller", catWeb, Difficulty.ADVANCED, 17.0, "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?auto=format&fit=crop&w=600&q=80", 4.82, 1599.0, 3199.0, 50, 780);
        addCourse("Vue.js 3 Production Patterns", "vuejs-3-production-patterns", "Composition API, Pinia, Vue Router, and Vite tooling.", "Alex Rivera", catWeb, Difficulty.INTERMEDIATE, 12.0, "https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=600&q=80", 4.84, 1299.0, 2599.0, 50, 890);
        addCourse("Node.js & Express.js API Engine", "nodejs-express-api-engine", "RESTful web services, middleware, JWT auth, and database integration.", "Alex Rivera", catWeb, Difficulty.INTERMEDIATE, 14.0, "https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80", 4.87, 1399.0, 2799.0, 50, 1430);
        addCourse("REST API Development Best Practices", "rest-api-development-best-practices", "API design patterns, OpenAPI/Swagger, rate limiting, and security headers.", "Prof. Sarah Jenkins", catWeb, Difficulty.BEGINNER, 9.0, "https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=600&q=80", 4.85, 1199.0, 2399.0, 50, 1150);

        // --- DATA & DATABASE (9 Courses) ---
        addCourse("SQL Fundamentals & Database Design", "sql-fundamentals-database-design", "Relational database concepts, DDL/DML queries, joins, and normal forms.", "Prof. Sarah Jenkins", catDb, Difficulty.BEGINNER, 11.0, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.88, 1199.0, 2399.0, 50, 1750);
        addCourse("MySQL Database Administration & Queries", "mysql-database-admin-queries", "Indexing, storage engines, transactions, query profiling, and backups.", "Elena Rostova", catDb, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.83, 1299.0, 2599.0, 50, 980);
        Course cOracle = addCourse("Oracle Database XE & Advanced PL/SQL", "oracle-db-advanced-sql", "Complex joins, CTEs, window functions, and Oracle PL/SQL stored procedures.", "Prof. Sarah Jenkins", catDb, Difficulty.INTERMEDIATE, 12.0, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.85, 1499.0, 2999.0, 50, 980);
        addCourse("Oracle PL/SQL Stored Procedures & Triggers", "oracle-plsql-procedures-triggers", "PL/SQL blocks, cursors, exception handling, packages, and database triggers.", "Prof. Sarah Jenkins", catDb, Difficulty.ADVANCED, 14.5, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.9, 1699.0, 3399.0, 50, 810);
        addCourse("PostgreSQL Administration & Performance Tuning", "postgresql-admin-performance-tuning", "MVCC, vacuuming, query planner, index types, and WAL replication.", "David Miller", catDb, Difficulty.ADVANCED, 15.0, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.89, 1599.0, 3199.0, 50, 740);
        addCourse("MongoDB Distributed NoSQL Architecture", "mongodb-distributed-nosql", "Document modeling, aggregation pipeline, sharding, and replica sets.", "Alex Rivera", catDb, Difficulty.INTERMEDIATE, 12.5, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.86, 1399.0, 2799.0, 50, 1120);
        addCourse("Database Management Systems (DBMS)", "database-management-systems-dbms", "Relational algebra, ACID properties, concurrency control, and recovery.", "Dr. Michael Vance", catDb, Difficulty.BEGINNER, 10.0, "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80", 4.79, 999.0, 1999.0, 50, 680);
        addCourse("Data Analytics & Visualization Masterclass", "data-analytics-visualization-masterclass", "Data cleaning, statistical metrics, Tableau/PowerBI, and storytelling.", "Elena Rostova", catDb, Difficulty.INTERMEDIATE, 16.0, "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80", 4.91, 1699.0, 3399.0, 50, 1520);
        addCourse("Python for Data Analysis with Pandas & NumPy", "python-data-analysis-pandas-numpy", "Data Manipulation, DataFrames, cleaning datasets, and data visualization.", "Elena Rostova", catDb, Difficulty.BEGINNER, 13.0, "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80", 4.87, 1399.0, 2799.0, 50, 1390);

        // --- CLOUD & DEVOPS (13 Courses) ---
        addCourse("Cloud Computing Fundamentals", "cloud-computing-fundamentals", "Cloud deployment models (IaaS, PaaS, SaaS), virtualization, and security.", "David Miller", catCloud, Difficulty.BEGINNER, 9.5, "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80", 4.8, 1099.0, 2199.0, 50, 1250);
        addCourse("AWS Cloud Practitioner Certification", "aws-cloud-practitioner-cert", "AWS core services, IAM, billing, global infrastructure, and security.", "David Miller", catCloud, Difficulty.BEGINNER, 12.0, "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80", 4.88, 1499.0, 2999.0, 50, 1680);
        addCourse("AWS Solutions Architect & Cloud Infrastructure", "aws-solutions-architect-cloud", "Master EC2, S3, RDS, Lambda, VPC, IAM, CloudFront, and cloud scalability.", "David Miller", catCloud, Difficulty.INTERMEDIATE, 15.0, "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80", 4.89, 1799.0, 3599.0, 50, 1150);
        addCourse("Microsoft Azure Cloud Administrator", "microsoft-azure-cloud-admin", "Azure Resource Manager, VMs, Virtual Networks, Entra ID, and storage.", "David Miller", catCloud, Difficulty.INTERMEDIATE, 14.0, "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80", 4.81, 1599.0, 3199.0, 50, 840);
        addCourse("Google Cloud Platform (GCP) Essentials", "google-cloud-platform-gcp-essentials", "Compute Engine, GKE, BigQuery, Cloud Storage, and IAM management.", "David Miller", catCloud, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80", 4.84, 1499.0, 2999.0, 50, 710);
        addCourse("Docker Containerization Masterclass", "docker-containerization-masterclass", "Dockerfiles, images, containers, volumes, networking, and multi-stage builds.", "David Miller", catCloud, Difficulty.BEGINNER, 10.5, "https://images.unsplash.com/photo-1605745341112-85968b19335b?auto=format&fit=crop&w=600&q=80", 4.9, 1299.0, 2599.0, 50, 1850);
        addCourse("Docker & Kubernetes: Microservices Orchestration", "docker-kubernetes-microservices", "Containerize enterprise applications and orchestrate resilient K8s pods.", "David Miller", catCloud, Difficulty.ADVANCED, 13.5, "https://images.unsplash.com/photo-1605745341112-85968b19335b?auto=format&fit=crop&w=600&q=80", 4.91, 1699.0, 3399.0, 50, 890);
        addCourse("DevOps Infrastructure & Engineering", "devops-infrastructure-engineering", "DevOps culture, automation pipelines, configuration management, and monitoring.", "David Miller", catCloud, Difficulty.INTERMEDIATE, 16.0, "https://images.unsplash.com/photo-1605745341112-85968b19335b?auto=format&fit=crop&w=600&q=80", 4.87, 1799.0, 3599.0, 50, 1140);
        addCourse("CI/CD Automation Pipelines", "cicd-automation-pipelines", "Build automated continuous integration and deployment pipelines.", "David Miller", catCloud, Difficulty.INTERMEDIATE, 11.0, "https://images.unsplash.com/photo-1605745341112-85968b19335b?auto=format&fit=crop&w=600&q=80", 4.85, 1399.0, 2799.0, 50, 920);
        addCourse("GitHub Actions & Automated Workflows", "github-actions-automated-workflows", "Automate testing, build artifacts, and deployment using GitHub Actions.", "Alex Rivera", catCloud, Difficulty.BEGINNER, 9.0, "https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?auto=format&fit=crop&w=600&q=80", 4.88, 1199.0, 2399.0, 50, 1050);
        addCourse("Terraform Infrastructure as Code (IaC)", "terraform-infrastructure-as-code", "Provision cloud infrastructure declaratively across AWS/Azure using HCL.", "David Miller", catCloud, Difficulty.ADVANCED, 13.0, "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80", 4.93, 1699.0, 3399.0, 50, 790);
        addCourse("Cloud Security & Compliance", "cloud-security-compliance", "Identity federation, data encryption, threat detection, and compliance standards.", "Marcus Thorne", catCloud, Difficulty.ADVANCED, 14.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.92, 1799.0, 3599.0, 50, 680);

        // --- CYBERSECURITY (6 Courses) ---
        addCourse("Cybersecurity Fundamentals", "cybersecurity-fundamentals", "Security principles, threat vectors, cryptography basics, and risk mitigation.", "Marcus Thorne", catSecurity, Difficulty.BEGINNER, 10.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.82, 1199.0, 2399.0, 50, 1310);
        addCourse("Network Security & Firewalls", "network-security-firewalls", "VPNs, IDS/IPS, Wireshark packet analysis, and firewall rule policies.", "Marcus Thorne", catSecurity, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.86, 1499.0, 2999.0, 50, 940);
        addCourse("Ethical Hacking & Web Penetration Testing", "ethical-hacking-web-security", "Vulnerability assessment, OWASP Top 10, SQLi, XSS, and exploit analysis.", "Marcus Thorne", catSecurity, Difficulty.ADVANCED, 17.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.94, 1799.0, 3599.0, 50, 1320);
        addCourse("Information Security Management", "information-security-management", "ISO 27001, security policies, incident response, and governance.", "Marcus Thorne", catSecurity, Difficulty.INTERMEDIATE, 12.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.8, 1399.0, 2799.0, 50, 610);
        addCourse("Penetration Testing Methodology", "penetration-testing-methodology", "Reconnaissance, scanning, exploitation, post-exploitation, and reporting.", "Marcus Thorne", catSecurity, Difficulty.ADVANCED, 15.5, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.91, 1899.0, 3799.0, 50, 750);
        addCourse("Web Application Security", "web-application-security", "Secure coding practices, authentication security, CSRF, and CORS policies.", "Marcus Thorne", catSecurity, Difficulty.INTERMEDIATE, 11.5, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.89, 1499.0, 2999.0, 50, 1080);

        // --- AUTOMATION, ELECTRONICS & HARDWARE (14 Courses) ---
        addCourse("Automation Architecture", "automation-architecture", "Master industrial automation systems, PLC logic, SCADA integration, and enterprise control loops.", "Dr. Michael Vance", catAuto, Difficulty.ADVANCED, 16.0, "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=600&q=80", 4.91, 1799.0, 3599.0, 50, 890);
        addCourse("Embedded Systems Engineering", "embedded-systems-engineering", "Design microcontroller firmware, RTOS, ARM architecture, and hardware interfaces.", "Dr. Michael Vance", catAuto, Difficulty.ADVANCED, 18.0, "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=600&q=80", 4.93, 1899.0, 3799.0, 50, 1120);
        addCourse("Sensors & IoT Systems Integration", "sensors-iot-systems-integration", "Interface analog/digital sensors, MQTT, Zigbee, microcontrollers, and IoT cloud gateways.", "Elena Rostova", catAuto, Difficulty.INTERMEDIATE, 14.0, "https://images.unsplash.com/photo-1558346490-a72e53ae2d4f?auto=format&fit=crop&w=600&q=80", 4.86, 1499.0, 2999.0, 50, 1450);
        addCourse("Industrial Electronics Design", "industrial-electronics-design", "Power electronics, signal conditioning, PCB routing, and industrial noise immunity.", "Dr. Michael Vance", catAuto, Difficulty.ADVANCED, 17.5, "https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?auto=format&fit=crop&w=600&q=80", 4.89, 1799.0, 3599.0, 50, 780);
        addCourse("Precision Agriculture Technologies", "precision-agriculture-technologies", "Smart farming sensors, automated irrigation, soil monitoring, and agricultural drones.", "Rachel Vance", catAuto, Difficulty.BEGINNER, 10.0, "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=600&q=80", 4.79, 999.0, 1999.0, 50, 620);
        addCourse("Building Management Systems (BMS)", "building-management-systems-bms", "HVAC control, BACnet protocols, smart lighting, energy efficiency, and security integration.", "David Miller", catAuto, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1541888946425-d0fbb186a5b7?auto=format&fit=crop&w=600&q=80", 4.82, 1399.0, 2799.0, 50, 840);
        addCourse("PLC & SCADA Industrial Automation", "plc-scada-industrial-automation", "Programmable Logic Controllers (Siemens/Allen Bradley), Ladder Logic, HMI, and SCADA monitoring.", "Dr. Michael Vance", catAuto, Difficulty.ADVANCED, 20.0, "https://images.unsplash.com/photo-1581092160607-ee22621dd758?auto=format&fit=crop&w=600&q=80", 4.95, 1999.0, 3999.0, 50, 1680);
        addCourse("Robotics Engineering & Kinematics", "robotics-engineering-kinematics", "Industrial robotic arms, ROS 2, inverse kinematics, actuators, and computer vision guidance.", "Dr. Michael Vance", catAuto, Difficulty.ADVANCED, 22.0, "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&w=600&q=80", 4.96, 2199.0, 4399.0, 50, 1350);
        addCourse("Motion Control Systems & Servo Motors", "motion-control-servo-motors", "Stepper and servo motor drives, PID closed-loop tuning, encoders, and multi-axis motion.", "Dr. Michael Vance", catAuto, Difficulty.INTERMEDIATE, 12.5, "https://images.unsplash.com/photo-1581092335397-9583fe92d232?auto=format&fit=crop&w=600&q=80", 4.84, 1399.0, 2799.0, 50, 710);
        addCourse("Process Control Systems & Instrumentation", "process-control-systems-instrumentation", "P&ID diagrams, transmitters, control valves, distributed control systems (DCS), and loop tuning.", "Dr. Michael Vance", catAuto, Difficulty.INTERMEDIATE, 14.0, "https://images.unsplash.com/photo-1581092160607-ee22621dd758?auto=format&fit=crop&w=600&q=80", 4.87, 1499.0, 2999.0, 50, 830);
        addCourse("Industrial Safety & Hazard Management", "industrial-safety-hazard-management", "OSHA safety standards, lockout/tagout (LOTO), functional safety (SIL), and risk assessment.", "Rachel Vance", catAuto, Difficulty.BEGINNER, 9.0, "https://images.unsplash.com/photo-1504307651254-35680f356dfd?auto=format&fit=crop&w=600&q=80", 4.77, 899.0, 1799.0, 50, 950);
        addCourse("PCB Design & Circuit Fabrication", "pcb-design-circuit-fabrication", "Schematic capture, multi-layer routing using KiCad/Altium, signal integrity, and Gerber export.", "Elena Rostova", catAuto, Difficulty.INTERMEDIATE, 15.0, "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=600&q=80", 4.88, 1499.0, 2999.0, 50, 1290);
        addCourse("Basics of 3D Printing & Additive Manufacturing", "basics-of-3d-printing-additive-manufacturing", "FDM/SLA 3D printers, CAD modeling, slicer settings, materials, and rapid prototyping.", "Elena Rostova", catAuto, Difficulty.BEGINNER, 8.5, "https://images.unsplash.com/photo-1581092160607-ee22621dd758?auto=format&fit=crop&w=600&q=80", 4.81, 999.0, 1999.0, 50, 1140);
        addCourse("Complete Domestic Civil, Electrical & Plumbing Technician", "domestic-civil-electrical-plumbing-technician", "Comprehensive hands-on training in residential wiring, plumbing hydraulics, civil repairs, and skill self-employability.", "Rachel Vance", catAuto, Difficulty.ADVANCED, 24.0, "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?auto=format&fit=crop&w=600&q=80", 4.92, 1999.0, 3999.0, 50, 1540);

        // --- INFORMATION TECHNOLOGY & EMERGING TECHNOLOGIES (12 Courses) ---
        addCourse("Industry 4.0 & Smart Manufacturing", "industry-4-0-smart-manufacturing", "Digital twin technology, industrial IoT, cyber-physical systems, and smart factory architecture.", "Prof. Sarah Jenkins", catEmerging, Difficulty.ADVANCED, 15.0, "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=600&q=80", 4.91, 1699.0, 3399.0, 50, 920);
        addCourse("Data Analytics Professional", "data-analytics-professional", "Transform raw data into business intelligence using SQL, Excel, Python Pandas, and Power BI.", "Elena Rostova", catEmerging, Difficulty.INTERMEDIATE, 16.0, "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80", 4.88, 1499.0, 2999.0, 50, 1850);
        addCourse("Cybersecurity Architecture & Defense", "cybersecurity-architecture-defense", "Zero trust models, threat hunting, SIEM, network defense, and incident response planning.", "Marcus Thorne", catEmerging, Difficulty.ADVANCED, 18.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.94, 1899.0, 3799.0, 50, 1420);
        addCourse("Artificial Intelligence Foundations", "artificial-intelligence-foundations", "Search algorithms, expert systems, neural networks, natural language processing, and AI ethics.", "Elena Rostova", catEmerging, Difficulty.ADVANCED, 20.0, "https://images.unsplash.com/photo-1620712943543-bcc4688e7485?auto=format&fit=crop&w=600&q=80", 4.96, 1999.0, 3999.0, 50, 2100);
        addCourse("Machine Learning & Predictive Modeling", "machine-learning-predictive-modeling", "Supervised/unsupervised learning, scikit-learn, regression, classification, and model deployment.", "Elena Rostova", catEmerging, Difficulty.ADVANCED, 18.5, "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=600&q=80", 4.93, 1899.0, 3799.0, 50, 1780);
        addCourse("Quantum Computing Principles", "quantum-computing-principles", "Qubits, quantum gates, entanglement, Qiskit framework, and quantum algorithms.", "Dr. Michael Vance", catEmerging, Difficulty.ADVANCED, 14.0, "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?auto=format&fit=crop&w=600&q=80", 4.92, 1999.0, 3999.0, 50, 640);
        addCourse("Red Hat Enterprise Linux (RHEL) Administration", "red-hat-linux-rhel-administration", "RHEL 9 system administration, storage management, SELinux policies, and Ansible automation.", "David Miller", catEmerging, Difficulty.INTERMEDIATE, 16.0, "https://images.unsplash.com/photo-1629654297299-c8506221ca97?auto=format&fit=crop&w=600&q=80", 4.89, 1599.0, 3199.0, 50, 1260);
        addCourse("CCNA Cisco Certified Network Associate", "ccna-cisco-network-associate", "IPv4/IPv6 addressing, switching, routing, wireless networking, and network security fundamentals.", "David Miller", catEmerging, Difficulty.INTERMEDIATE, 20.0, "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?auto=format&fit=crop&w=600&q=80", 4.91, 1699.0, 3399.0, 50, 1980);
        addCourse("CCNP Enterprise Network Architecture", "ccnp-enterprise-network-architecture", "Advanced dual-stack routing (OSPF, BGP), SD-WAN, network automation, and enterprise infrastructure.", "David Miller", catEmerging, Difficulty.ADVANCED, 24.0, "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?auto=format&fit=crop&w=600&q=80", 4.95, 2199.0, 4399.0, 50, 890);
        addCourse("CompTIA A+ IT Technician", "comptia-a-plus-it-technician", "Hardware installation, OS troubleshooting, mobile devices, security protocols, and operational procedures.", "Rachel Vance", catEmerging, Difficulty.BEGINNER, 14.0, "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?auto=format&fit=crop&w=600&q=80", 4.82, 1199.0, 2399.0, 50, 1420);
        addCourse("CompTIA Network+ Certification Guide", "comptia-network-plus-certification", "Network topologies, Ethernet standards, OSI layers, cloud concepts, and network troubleshooting.", "David Miller", catEmerging, Difficulty.INTERMEDIATE, 15.0, "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?auto=format&fit=crop&w=600&q=80", 4.86, 1399.0, 2799.0, 50, 1180);
        addCourse("EC-Council Ethical Hacking Certification", "ec-council-ethical-hacking-programs", "CEH curriculum: footprinting, scanning, malware analysis, social engineering, and wireless hacking.", "Marcus Thorne", catEmerging, Difficulty.ADVANCED, 22.0, "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80", 4.97, 2299.0, 4599.0, 50, 1650);

        // --- RENEWABLE ENERGY & SMART SYSTEMS (3 Courses) ---
        addCourse("Solar Panel Installation & Maintenance", "solar-panel-installation-maintenance", "Photovoltaic panel sizing, inverter wiring, grid-tie/off-grid systems, safety, and routine maintenance.", "David Miller", catRenewable, Difficulty.BEGINNER, 11.0, "https://images.unsplash.com/photo-1509391365360-2e959784a276?auto=format&fit=crop&w=600&q=80", 4.83, 1099.0, 2199.0, 50, 980);
        addCourse("Electric Vehicle Management & Charging Solutions", "electric-vehicle-charging-solutions", "EV battery management systems (BMS), AC/DC fast charging stations, power electronics, and grid load balancing.", "Dr. Michael Vance", catRenewable, Difficulty.INTERMEDIATE, 14.0, "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=600&q=80", 4.88, 1499.0, 2999.0, 50, 1150);
        addCourse("Electric Transient Analysis Program (ETAP)", "electric-transient-analysis-program-etap", "Power system modeling, short circuit analysis, load flow, protection coordination, and arc flash safety in ETAP.", "Dr. Michael Vance", catRenewable, Difficulty.ADVANCED, 18.0, "https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?auto=format&fit=crop&w=600&q=80", 4.94, 1899.0, 3799.0, 50, 720);

        // --- LANGUAGE & PROFESSIONAL DEVELOPMENT (12 Courses) ---
        addCourse("English Communication for Professionals", "english-communication-professionals", "Business English grammar, vocabulary, email etiquette, presentation delivery, and interview skills.", "Rachel Vance", catLang, Difficulty.BEGINNER, 10.0, "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?auto=format&fit=crop&w=600&q=80", 4.82, 899.0, 1799.0, 50, 2100);
        addCourse("French Language Essentials", "french-language-essentials", "Beginner French conversational vocabulary, pronunciation, basic grammar, and greeting etiquette.", "Rachel Vance", catLang, Difficulty.BEGINNER, 9.0, "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=600&q=80", 4.79, 899.0, 1799.0, 50, 1120);
        addCourse("Spanish Language Essentials", "spanish-language-essentials", "Spanish pronunciation, common expressions, verb conjugations, and everyday conversation.", "Rachel Vance", catLang, Difficulty.BEGINNER, 9.0, "https://images.unsplash.com/photo-1543783207-ec64e4d95325?auto=format&fit=crop&w=600&q=80", 4.81, 899.0, 1799.0, 50, 1340);
        addCourse("German Language Essentials", "german-language-essentials", "German A1 basics, sentence structure, workplace vocabulary, and practical dialogue.", "Rachel Vance", catLang, Difficulty.BEGINNER, 9.5, "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?auto=format&fit=crop&w=600&q=80", 4.8, 899.0, 1799.0, 50, 980);
        addCourse("Italian Language Essentials", "italian-language-essentials", "Italian conversational phrases, cultural context, travel/business vocabulary, and pronunciation.", "Rachel Vance", catLang, Difficulty.BEGINNER, 8.5, "https://images.unsplash.com/photo-1516483638261-f4dbaf036963?auto=format&fit=crop&w=600&q=80", 4.78, 899.0, 1799.0, 50, 760);
        addCourse("Portuguese Language Essentials", "portuguese-language-essentials", "Brazilian and European Portuguese fundamentals, basic grammar, and conversation skills.", "Rachel Vance", catLang, Difficulty.BEGINNER, 8.5, "https://images.unsplash.com/photo-1555881400-74d7acaacd8b?auto=format&fit=crop&w=600&q=80", 4.77, 899.0, 1799.0, 50, 620);
        addCourse("Chinese Mandarin Conversational Skills", "chinese-mandarin-conversational", "Pinyin, essential Chinese characters, business greetings, and Mandarin dialogue fluency.", "Rachel Vance", catLang, Difficulty.INTERMEDIATE, 12.0, "https://images.unsplash.com/photo-1508804185872-d7badad00f7d?auto=format&fit=crop&w=600&q=80", 4.86, 1199.0, 2399.0, 50, 890);
        addCourse("Japanese Language & Cultural Etiquette", "japanese-language-cultural-etiquette", "Hiragana, Katakana, basic Kanji, workplace honorifics, and conversational Japanese.", "Rachel Vance", catLang, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&w=600&q=80", 4.9, 1299.0, 2599.0, 50, 1150);
        addCourse("Korean Language Foundations", "korean-language-foundations", "Hangul script, Korean grammar structures, formal/informal honorifics, and everyday speech.", "Rachel Vance", catLang, Difficulty.INTERMEDIATE, 11.5, "https://images.unsplash.com/photo-1538485399081-7191377e8241?auto=format&fit=crop&w=600&q=80", 4.87, 1199.0, 2399.0, 50, 1040);
        addCourse("Russian Language Essentials", "russian-language-essentials", "Cyrillic alphabet, Russian case system, business dialogue, and conversational basics.", "Elena Rostova", catLang, Difficulty.INTERMEDIATE, 12.5, "https://images.unsplash.com/photo-1513326738677-b964603b136d?auto=format&fit=crop&w=600&q=80", 4.83, 1199.0, 2399.0, 50, 680);
        addCourse("Employability & Career Skills", "employability-career-skills", "Resume building, LinkedIn optimization, behavioral interviewing, and workplace soft skills.", "Rachel Vance", catLang, Difficulty.BEGINNER, 8.0, "https://images.unsplash.com/photo-1521737711867-e3b97375f902?auto=format&fit=crop&w=600&q=80", 4.88, 799.0, 1599.0, 50, 2450);
        addCourse("Digital Marketing Strategy & Analytics", "digital-marketing-strategy-analytics", "SEO, SEM, social media marketing, conversion funnels, and data-driven analytics.", "Rachel Vance", catLang, Difficulty.INTERMEDIATE, 12.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.84, 1199.0, 2399.0, 50, 1150);

        // --- MANAGEMENT (9 Courses) ---
        addCourse("Project Management Principles", "project-management-principles", "Project lifecycle, scope definition, WBS, risk management, and budgeting.", "Rachel Vance", catMgmt, Difficulty.BEGINNER, 10.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.8, 1199.0, 2399.0, 50, 1040);
        addCourse("Product Management & Strategy", "product-management-strategy", "Product roadmap, user personas, MVP development, metrics, and GTM strategy.", "Rachel Vance", catMgmt, Difficulty.INTERMEDIATE, 13.5, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.89, 1599.0, 3199.0, 50, 890);
        addCourse("Business Analysis & Requirements Engineering", "business-analysis-requirements", "BRD documents, user stories, process flowcharts, and stakeholder interviews.", "Rachel Vance", catMgmt, Difficulty.BEGINNER, 11.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.81, 1299.0, 2599.0, 50, 760);
        addCourse("Business Communication & Executive Presence", "business-communication-executive-presence", "Persuasive writing, presentation skills, meeting management, and negotiation.", "Rachel Vance", catMgmt, Difficulty.BEGINNER, 8.5, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.78, 999.0, 1999.0, 50, 690);
        addCourse("Leadership & Team Management", "leadership-team-management", "Team dynamics, conflict resolution, emotional intelligence, and delegation.", "Rachel Vance", catMgmt, Difficulty.INTERMEDIATE, 12.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.86, 1399.0, 2799.0, 50, 830);
        addCourse("Agile & Scrum Project Management Certification", "agile-scrum-project-management", "Scrum ceremonies, sprint planning, Jira boards, and servant leadership.", "Rachel Vance", catMgmt, Difficulty.BEGINNER, 9.5, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.82, 1299.0, 2599.0, 50, 760);
        addCourse("Digital Marketing & Growth Analytics", "digital-marketing-growth-analytics", "SEO, SEM, social media marketing, conversion funnels, and Google Analytics.", "Rachel Vance", catMgmt, Difficulty.BEGINNER, 12.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.84, 1199.0, 2399.0, 50, 1150);
        addCourse("Entrepreneurship & Startup Fundamentals", "entrepreneurship-startup-fundamentals", "Business models, pitching investors, cap tables, legal basics, and scaling.", "Rachel Vance", catMgmt, Difficulty.INTERMEDIATE, 14.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.87, 1499.0, 2999.0, 50, 920);
        addCourse("Operations Management & Supply Chain", "operations-management-supply-chain", "Process optimization, inventory control, logistics, Lean, and Six Sigma.", "Rachel Vance", catMgmt, Difficulty.INTERMEDIATE, 13.0, "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80", 4.8, 1399.0, 2799.0, 50, 580);

        // --- Modules & Lessons for Course 1 & Course FS ---
        Module m1J = Module.builder().course(c1).title("Module 1: Java 17 Language Essentials").moduleOrder(1).build();
        moduleRepository.save(m1J);
        Lesson l1J = Lesson.builder().module(m1J).title("1.1 Introduction to Java 17 LTS & JVM Architecture").durationMinutes(25).lessonOrder(1)
                .content("### Welcome to Java 17 LTS\nJava 17 brings Records, Sealed Classes, Pattern Matching, and GC performance enhancements.\n```java\npublic record Student(String name, String email) {}\n```").build();
        Lesson l2J = Lesson.builder().module(m1J).title("1.2 Object-Oriented Design & Clean Code Principles").durationMinutes(30).lessonOrder(2)
                .content("### Clean OOP Principles\nLearn Encapsulation, Inheritance, Polymorphism, and Interface Contracts.").build();
        lessonRepository.saveAll(List.of(l1J, l2J));

        Assessment assJava = Assessment.builder().course(c1).title("Java 17 Practice Test").passingScore(70).timeLimitMinutes(15).build();
        assessmentRepository.save(assJava);
        Question q1J = Question.builder().assessment(assJava).questionText("Which Java 17 feature creates an immutable data carrier class?").points(10).build();
        questionRepository.save(q1J);
        questionOptionRepository.saveAll(List.of(
                QuestionOption.builder().question(q1J).optionText("Sealed Classes").isCorrect(false).build(),
                QuestionOption.builder().question(q1J).optionText("Record Classes").isCorrect(true).build(),
                QuestionOption.builder().question(q1J).optionText("Text Blocks").isCorrect(false).build()
        ));

        // Enrollments & Reviews
        Enrollment e1 = Enrollment.builder().user(student).course(c1).status(EnrollmentStatus.ACTIVE).enrolledAt(LocalDateTime.now().minusDays(5)).build();
        Enrollment e2 = Enrollment.builder().user(student).course(cFS).status(EnrollmentStatus.ACTIVE).enrolledAt(LocalDateTime.now().minusDays(2)).build();
        enrollmentRepository.saveAll(List.of(e1, e2));

        UserProgress p1 = UserProgress.builder().user(student).course(c1).lesson(l1J).completed(true).completedAt(LocalDateTime.now().minusDays(3)).build();
        UserProgress p2 = UserProgress.builder().user(student).course(c1).lesson(l2J).completed(true).completedAt(LocalDateTime.now().minusDays(1)).build();
        userProgressRepository.saveAll(List.of(p1, p2));

        CourseReview r1 = CourseReview.builder().course(c1).user(student).rating(5).comment("Exceptional course! Clear explanation of Java 17 Records and JVM internals.").build();
        CourseReview r2 = CourseReview.builder().course(cFS).user(student).rating(5).comment("The Spring Boot & JWT security sections are top notch! Highly recommended.").build();
        courseReviewRepository.saveAll(List.of(r1, r2));

        // Course Q&A
        CourseQA qa1 = CourseQA.builder().course(c1).student(student).questionText("Will there be any project on Spring Boot 3 in this course?").answerText("Yes, the final capstone is a full Spring Boot 3 microservice.").askedAt(LocalDateTime.now().minusDays(2)).answeredAt(LocalDateTime.now().minusDays(1)).build();
        CourseQA qa2 = CourseQA.builder().course(cFS).student(student).questionText("How is the frontend integrated?").askedAt(LocalDateTime.now().minusHours(5)).build();
        courseQARepository.saveAll(List.of(qa1, qa2));

        // Earnings
        InstructorEarnings ie1 = InstructorEarnings.builder().instructor(instructor).course(c1).enrollment(e1).amount(1499.0 * 0.7).description("Enrollment royalty").earnedAt(LocalDateTime.now().minusDays(5)).build();
        InstructorEarnings ie2 = InstructorEarnings.builder().instructor(instructor).course(cFS).enrollment(e2).amount(1999.0 * 0.7).description("Enrollment royalty").earnedAt(LocalDateTime.now().minusDays(2)).build();
        instructorEarningsRepository.saveAll(List.of(ie1, ie2));

        // Course Batches & Schedules
        CourseBatch cb1 = CourseBatch.builder().course(c1).name("August Batch 2026").startDate(java.time.LocalDate.now().minusDays(10)).endDate(java.time.LocalDate.now().plusDays(50)).build();
        courseBatchRepository.save(cb1);

        Schedule sch1 = Schedule.builder().courseBatch(cb1).title("Live Q&A Session").description("Discussing JVM internals").startTime(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)).endTime(LocalDateTime.now().plusDays(1).withHour(19).withMinute(0)).meetingLink("https://meet.google.com/abc-defg-hij").build();
        Schedule sch2 = Schedule.builder().courseBatch(cb1).title("Weekly Sync").description("Project reviews").startTime(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0)).endTime(LocalDateTime.now().plusDays(3).withHour(11).withMinute(30)).meetingLink("https://zoom.us/j/123456").build();
        scheduleRepository.saveAll(List.of(sch1, sch2));
    }

    private Course addCourse(String title, String slug, String desc, String instructor, Category cat, Difficulty diff, Double hours, String thumb, Double rating, Double price, Double origPrice, Integer discount, Integer enrolled) {
        Course course = Course.builder()
                .title(title)
                .slug(slug)
                .description(desc)
                .instructorName(instructor)
                .category(cat)
                .difficulty(diff)
                .durationHours(hours)
                .thumbnailUrl(thumb)
                .rating(rating)
                .price(price)
                .originalPrice(origPrice)
                .discountPercentage(discount)
                .enrollmentCount(enrolled)
                .build();
        Course savedCourse = courseRepository.save(course);

        int moduleCount = 3;
        if (diff == Difficulty.INTERMEDIATE) {
            moduleCount = 6;
        } else if (diff == Difficulty.ADVANCED) {
            moduleCount = 10;
        }

        List<Module> modules = new ArrayList<>();
        List<Lesson> lessons = new ArrayList<>();

        for (int m = 1; m <= moduleCount; m++) {
            String modTitle;
            if (m == 1) modTitle = "Module 1: Foundations & System Architecture Setup";
            else if (m == 2) modTitle = "Module 2: Core Engineering & Syntax Deep Dive";
            else if (m == 3) modTitle = "Module 3: Hands-On Development & Lab Practice";
            else if (m == 4) modTitle = "Module 4: Advanced Systems & Architectural Patterns";
            else if (m == 5) modTitle = "Module 5: Performance Tuning & Optimization";
            else if (m == 6) modTitle = "Module 6: Security, Testing & Industrial Standards";
            else if (m == 7) modTitle = "Module 7: Enterprise Distributed Workflows";
            else if (m == 8) modTitle = "Module 8: Cloud Deployment & CI/CD Pipelines";
            else if (m == 9) modTitle = "Module 9: Microservices & System Resilience";
            else modTitle = "Module 10: Capstone Project & Skill Certification";

            Module module = Module.builder().course(savedCourse).title(modTitle).moduleOrder(m).build();
            modules.add(module);
        }
        moduleRepository.saveAll(modules);

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int mNum = i + 1;

            Lesson l1 = Lesson.builder()
                    .module(module)
                    .title(mNum + ".1 " + title + " — Part A: Architecture & Principles")
                    .durationMinutes(25 + (mNum * 2))
                    .lessonOrder(1)
                    .content("### Welcome to " + title + " — Module " + mNum + "\nExplore core principles, architectural guidelines, environment setup, and foundational concepts.")
                    .build();

            Lesson l2 = Lesson.builder()
                    .module(module)
                    .title(mNum + ".2 " + title + " — Part B: Practical Hands-On Lab")
                    .durationMinutes(30 + (mNum * 3))
                    .lessonOrder(2)
                    .content("### Practical Hands-On Lab — Module " + mNum + "\nWrite production code, run test cases, profile memory/execution performance, and apply industry best practices.")
                    .build();

            lessons.add(l1);
            lessons.add(l2);
        }
        lessonRepository.saveAll(lessons);

        return savedCourse;
    }
}
