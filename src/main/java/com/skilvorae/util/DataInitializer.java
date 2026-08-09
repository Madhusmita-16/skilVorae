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

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initialization.");
            return;
        }

        log.info("Seeding SkilVorae production demo data...");

        // 1. Seed Users (Student, Instructor, Admin)
        User student = User.builder()
                .fullName("Alex Morgan")
                .email("student@skilvorae.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.STUDENT)
                .build();
        userRepository.save(student);

        User instructor = User.builder()
                .fullName("Prof. Sarah Jenkins")
                .email("instructor@skilvorae.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.INSTRUCTOR)
                .build();
        userRepository.save(instructor);

        User admin = User.builder()
                .fullName("System Administrator")
                .email("admin@skilvorae.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);

        // 2. Seed Categories
        Category catProg = Category.builder().name("Programming Languages").slug("programming").description("Core Java, Python, C++, Go, Rust, C#, Kotlin, Swift & Modern Syntax.").icon("code").build();
        Category catCS = Category.builder().name("Core Computer Science").slug("computer-science").description("Data Structures & Algorithms, OOP Architecture, & System Design.").icon("cpu").build();
        Category catFullStack = Category.builder().name("Advanced Full Stack").slug("full-stack").description("Java Spring Boot, MERN, MEAN, .NET & Python Django Full Stack Systems.").icon("layers").build();
        Category catWeb = Category.builder().name("Web Technologies").slug("web-tech").description("HTML5, CSS3, JavaScript, React.js, Angular, Vue.js & REST APIs.").icon("globe").build();
        Category catDb = Category.builder().name("Database & Data Analytics").slug("databases").description("Oracle SQL/PLSQL, PostgreSQL, MongoDB, Data Engineering & Analytics.").icon("database").build();
        Category catCloud = Category.builder().name("IT & Cloud Engineering").slug("cloud-it").description("AWS, Linux Administration, Docker, Kubernetes & DevOps CI/CD.").icon("cloud").build();
        Category catSecurity = Category.builder().name("Cybersecurity").slug("cybersecurity").description("Ethical Hacking, Network Security, Web App Penetration Testing.").icon("shield").build();
        Category catMgmt = Category.builder().name("Management & Business").slug("management").description("Project Management, Agile/Scrum, Product Strategy & Leadership.").icon("briefcase").build();

        categoryRepository.saveAll(List.of(catProg, catCS, catFullStack, catWeb, catDb, catCloud, catSecurity, catMgmt));

        // Helper method for course building
        createCourseCatalog(catProg, catCS, catFullStack, catWeb, catDb, catCloud, catSecurity, catMgmt, student, instructor);

        // Seed Notifications for Student
        Notification n1 = Notification.builder().user(student).title("Welcome to SkilVorae! 🚀").message("Explore our enterprise course catalog and enroll to boost your software career.").type("SYSTEM").build();
        Notification n2 = Notification.builder().user(student).title("Java 17 Masterclass Progress").message("You completed 2 lessons in Java 17 Enterprise Masterclass! Keep up the momentum.").type("ENROLLMENT").build();
        notificationRepository.saveAll(List.of(n1, n2));

        log.info("SkilVorae demo seed data created successfully!");
        log.info("Default Demo Student: student@skilvorae.com / password123");
        log.info("Default Demo Instructor: instructor@skilvorae.com / password123");
        log.info("Default Demo Admin: admin@skilvorae.com / password123");
    }

    private void createCourseCatalog(Category catProg, Category catCS, Category catFullStack, Category catWeb, Category catDb, Category catCloud, Category catSecurity, Category catMgmt, User student, User instructor) {
        // Course 1: Java 17 Enterprise Masterclass
        Course cJava = Course.builder()
                .title("Java 17 Enterprise Masterclass: From Fundamentals to Architecture")
                .slug("java-17-enterprise-masterclass")
                .description("Master Java 17 LTS features, Object-Oriented Design, Collections Framework, Streams API, Lambda Expressions, and Multithreading.")
                .instructorName("Prof. Sarah Jenkins")
                .category(catProg)
                .difficulty(Difficulty.BEGINNER)
                .durationHours(14.5)
                .thumbnailUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=600&q=80")
                .rating(4.9)
                .enrollmentCount(1240)
                .build();
        courseRepository.save(cJava);

        Module m1J = Module.builder().course(cJava).title("Module 1: Java 17 Language Essentials").moduleOrder(1).build();
        moduleRepository.save(m1J);
        Lesson l1J = Lesson.builder().module(m1J).title("1.1 Introduction to Java 17 LTS & JVM Architecture").durationMinutes(25).lessonOrder(1)
                .content("### Welcome to Java 17 LTS\nJava 17 brings Records, Sealed Classes, Pattern Matching, and GC performance enhancements.\n```java\npublic record Student(String name, String email) {}\n```").build();
        Lesson l2J = Lesson.builder().module(m1J).title("1.2 Object-Oriented Design & Clean Code Principles").durationMinutes(30).lessonOrder(2)
                .content("### Clean OOP Principles\nLearn Encapsulation, Inheritance, Polymorphism, and Interface Contracts.").build();
        lessonRepository.saveAll(List.of(l1J, l2J));

        Assessment assJava = Assessment.builder().course(cJava).title("Java 17 Practice Test").passingScore(70).timeLimitMinutes(15).build();
        assessmentRepository.save(assJava);
        Question q1J = Question.builder().assessment(assJava).questionText("Which Java 17 feature creates an immutable data carrier class?").points(10).build();
        questionRepository.save(q1J);
        questionOptionRepository.saveAll(List.of(
                QuestionOption.builder().question(q1J).optionText("Sealed Classes").isCorrect(false).build(),
                QuestionOption.builder().question(q1J).optionText("Record Classes").isCorrect(true).build(),
                QuestionOption.builder().question(q1J).optionText("Text Blocks").isCorrect(false).build()
        ));

        // Course 2: Java Full Stack Development (Spring Boot & Microservices)
        Course cJavaFS = Course.builder()
                .title("Java Full Stack Development: Spring Boot 3 & Microservices")
                .slug("java-full-stack-spring-boot-microservices")
                .description("Build enterprise full-stack applications with Spring Boot 3, Spring Security 6, JWT, Thymeleaf, Spring Data JPA, and Oracle Database.")
                .instructorName("Prof. Sarah Jenkins")
                .category(catFullStack)
                .difficulty(Difficulty.ADVANCED)
                .durationHours(22.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=600&q=80")
                .rating(4.95)
                .enrollmentCount(1890)
                .build();
        courseRepository.save(cJavaFS);

        Module m1FS = Module.builder().course(cJavaFS).title("Module 1: Spring Boot 3 & REST API Architecture").moduleOrder(1).build();
        moduleRepository.save(m1FS);
        Lesson l1FS = Lesson.builder().module(m1FS).title("1.1 Building Scalable RESTful Services").durationMinutes(35).lessonOrder(1)
                .content("### Spring Boot REST Controllers\nImplement `@RestController`, `@GetMapping`, and DTO serialization.").build();
        lessonRepository.save(l1FS);

        // Course 3: Data Structures & Algorithms Masterclass
        Course cDSA = Course.builder()
                .title("Data Structures & Algorithms Masterclass in Java")
                .slug("data-structures-algorithms-java")
                .description("Ace coding interviews! Master Arrays, Linked Lists, Trees, Graphs, Sorting, Dynamic Programming, and Big-O notation.")
                .instructorName("Dr. Michael Vance")
                .category(catCS)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationHours(18.5)
                .thumbnailUrl("https://images.unsplash.com/photo-1509228468518-180dd4864904?auto=format&fit=crop&w=600&q=80")
                .rating(4.88)
                .enrollmentCount(2100)
                .build();
        courseRepository.save(cDSA);

        Module m1DSA = Module.builder().course(cDSA).title("Module 1: Arrays & Dynamic Lists").moduleOrder(1).build();
        moduleRepository.save(m1DSA);
        Lesson l1DSA = Lesson.builder().module(m1DSA).title("1.1 Big-O Analysis & Array Manipulation").durationMinutes(30).lessonOrder(1)
                .content("### Time & Space Complexity\nAnalyze O(1), O(log n), O(n), and O(n^2) algorithms.").build();
        lessonRepository.save(l1DSA);

        // Course 4: Python for Data Processing & Backend
        Course cPy = Course.builder()
                .title("Python 3 Programming: Beginner to Advanced")
                .slug("python-3-programming-mastery")
                .description("Learn Python 3 syntaxes, data structures, OOP, file handling, web scraping, and automation scripts.")
                .instructorName("Elena Rostova")
                .category(catProg)
                .difficulty(Difficulty.BEGINNER)
                .durationHours(16.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=600&q=80")
                .rating(4.85)
                .enrollmentCount(1560)
                .build();
        courseRepository.save(cPy);

        // Course 5: Oracle SQL & PL/SQL Database Engineering
        Course cOracle = Course.builder()
                .title("Oracle Database XE & Advanced PL/SQL Query Optimization")
                .slug("oracle-db-advanced-sql")
                .description("Design normalized schemas, write complex joins, CTEs, window functions, and optimize performance using Oracle indexes.")
                .instructorName("Prof. Sarah Jenkins")
                .category(catDb)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationHours(12.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=600&q=80")
                .rating(4.85)
                .enrollmentCount(980)
                .build();
        courseRepository.save(cOracle);

        // Course 6: Modern Web Development (HTML5, CSS3, JS)
        Course cWeb = Course.builder()
                .title("Modern Web Development: HTML5, CSS3 & JavaScript ES6+")
                .slug("modern-web-development-html-css-js")
                .description("Build beautiful responsive websites with CSS flexbox, grid, animations, glassmorphism UI, and interactive ES6+ JS.")
                .instructorName("Alex Rivera")
                .category(catWeb)
                .difficulty(Difficulty.BEGINNER)
                .durationHours(11.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=600&q=80")
                .rating(4.8)
                .enrollmentCount(1420)
                .build();
        courseRepository.save(cWeb);

        // Course 7: MERN Stack Development
        Course cMern = Course.builder()
                .title("MERN Stack Development: MongoDB, Express, React & Node")
                .slug("mern-stack-development")
                .description("Full stack JavaScript development with MongoDB, Express server, React UI components, and Node.js backend.")
                .instructorName("Alex Rivera")
                .category(catFullStack)
                .difficulty(Difficulty.ADVANCED)
                .durationHours(20.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80")
                .rating(4.92)
                .enrollmentCount(1650)
                .build();
        courseRepository.save(cMern);

        // Course 8: AWS Solutions Architect & Cloud Engineering
        Course cAws = Course.builder()
                .title("AWS Certified Solutions Architect & Cloud Infrastructure")
                .slug("aws-solutions-architect-cloud")
                .description("Master EC2, S3, RDS, Lambda, VPC, IAM, CloudFront, and cloud scalability best practices.")
                .instructorName("David Miller")
                .category(catCloud)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationHours(15.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80")
                .rating(4.89)
                .enrollmentCount(1150)
                .build();
        courseRepository.save(cAws);

        // Course 9: Docker & Kubernetes Microservices Orchestration
        Course cK8s = Course.builder()
                .title("Docker & Kubernetes: Enterprise Microservices Orchestration")
                .slug("docker-kubernetes-microservices")
                .description("Containerize enterprise applications with Docker and orchestrate resilient deployments with Kubernetes pods & services.")
                .instructorName("David Miller")
                .category(catCloud)
                .difficulty(Difficulty.ADVANCED)
                .durationHours(13.5)
                .thumbnailUrl("https://images.unsplash.com/photo-1605745341112-85968b19335b?auto=format&fit=crop&w=600&q=80")
                .rating(4.91)
                .enrollmentCount(890)
                .build();
        courseRepository.save(cK8s);

        // Course 10: Ethical Hacking & Web Application Security
        Course cSec = Course.builder()
                .title("Ethical Hacking & Web Application Penetration Testing")
                .slug("ethical-hacking-web-security")
                .description("Learn vulnerability assessment, OWASP Top 10, SQL injection, XSS prevention, and secure coding practices.")
                .instructorName("Marcus Thorne")
                .category(catSecurity)
                .difficulty(Difficulty.ADVANCED)
                .durationHours(17.0)
                .thumbnailUrl("https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80")
                .rating(4.94)
                .enrollmentCount(1320)
                .build();
        courseRepository.save(cSec);

        // Course 11: Project Management & Agile / Scrum Leadership
        Course cPmp = Course.builder()
                .title("Agile & Scrum Project Management Certification")
                .slug("agile-scrum-project-management")
                .description("Lead software teams with Agile methodologies, Scrum sprints, Jira tracking, Kanban, and stakeholder communications.")
                .instructorName("Rachel Vance")
                .category(catMgmt)
                .difficulty(Difficulty.BEGINNER)
                .durationHours(9.5)
                .thumbnailUrl("https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=600&q=80")
                .rating(4.82)
                .enrollmentCount(760)
                .build();
        courseRepository.save(cPmp);

        // Add 3 Student Enrollments for immediate dashboard data readiness
        Enrollment e1 = Enrollment.builder().user(student).course(cJava).status(EnrollmentStatus.ACTIVE).enrolledAt(LocalDateTime.now().minusDays(5)).build();
        Enrollment e2 = Enrollment.builder().user(student).course(cJavaFS).status(EnrollmentStatus.ACTIVE).enrolledAt(LocalDateTime.now().minusDays(2)).build();
        enrollmentRepository.saveAll(List.of(e1, e2));

        UserProgress p1 = UserProgress.builder().user(student).course(cJava).lesson(l1J).completed(true).completedAt(LocalDateTime.now().minusDays(3)).build();
        UserProgress p2 = UserProgress.builder().user(student).course(cJava).lesson(l2J).completed(true).completedAt(LocalDateTime.now().minusDays(1)).build();
        userProgressRepository.saveAll(List.of(p1, p2));

        // Seed Reviews
        CourseReview r1 = CourseReview.builder().course(cJava).user(student).rating(5).comment("Exceptional course! Clear explanation of Java 17 Records and JVM internals.").build();
        CourseReview r2 = CourseReview.builder().course(cJavaFS).user(student).rating(5).comment("The Spring Boot & JWT security sections are top notch! Highly recommended for backend developers.").build();
        courseReviewRepository.saveAll(List.of(r1, r2));
    }
}
