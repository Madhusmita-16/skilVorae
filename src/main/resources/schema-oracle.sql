-- ============================================================================
-- SkilVorae Database DDL Schema Script (Oracle DB Enterprise Edition / XE)
-- Target: Oracle 11g / 12c / 18c / 19c / 21c / XE
-- ============================================================================

-- 1. USERS TABLE & SEQUENCE
CREATE TABLE users (
    id NUMBER(19) PRIMARY KEY,
    full_name VARCHAR2(100) NOT NULL,
    email VARCHAR2(150) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) NOT NULL,
    reset_otp VARCHAR2(10),
    otp_expiry TIMESTAMP,
    phone VARCHAR2(30),
    qualification VARCHAR2(100),
    area_of_interest VARCHAR2(100),
    expertise VARCHAR2(200),
    years_of_experience NUMBER(5),
    bio VARCHAR2(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE seq_users START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 2. CATEGORIES TABLE & SEQUENCE
CREATE TABLE categories (
    id NUMBER(19) PRIMARY KEY,
    name VARCHAR2(100) NOT NULL UNIQUE,
    slug VARCHAR2(100) NOT NULL UNIQUE,
    description VARCHAR2(500),
    icon VARCHAR2(50)
);

CREATE SEQUENCE seq_categories START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 3. COURSES TABLE & SEQUENCE
CREATE TABLE courses (
    id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    slug VARCHAR2(200) NOT NULL UNIQUE,
    description VARCHAR2(2000) NOT NULL,
    instructor_name VARCHAR2(100) NOT NULL,
    category_id NUMBER(19) NOT NULL,
    difficulty VARCHAR2(20) NOT NULL,
    duration_hours NUMBER(10,2),
    thumbnail_url VARCHAR2(500),
    rating NUMBER(3,2) DEFAULT 4.8,
    enrollment_count NUMBER(10) DEFAULT 0,
    price NUMBER(10,2),
    original_price NUMBER(10,2),
    discount_percentage NUMBER(5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_courses_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_courses START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 4. MODULES TABLE & SEQUENCE
CREATE TABLE modules (
    id NUMBER(19) PRIMARY KEY,
    course_id NUMBER(19) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    module_order NUMBER(5) NOT NULL,
    CONSTRAINT fk_modules_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_modules START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 5. LESSONS TABLE & SEQUENCE
CREATE TABLE lessons (
    id NUMBER(19) PRIMARY KEY,
    module_id NUMBER(19) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    content NCLOB,
    video_url VARCHAR2(500),
    duration_minutes NUMBER(5) NOT NULL,
    lesson_order NUMBER(5) NOT NULL,
    CONSTRAINT fk_lessons_module FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_lessons START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 6. ASSESSMENTS TABLE & SEQUENCE
CREATE TABLE assessments (
    id NUMBER(19) PRIMARY KEY,
    course_id NUMBER(19) NOT NULL UNIQUE,
    title VARCHAR2(200) NOT NULL,
    passing_score NUMBER(5) DEFAULT 70 NOT NULL,
    time_limit_minutes NUMBER(5) DEFAULT 15 NOT NULL,
    CONSTRAINT fk_assessments_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_assessments START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 7. QUESTIONS TABLE & SEQUENCE
CREATE TABLE questions (
    id NUMBER(19) PRIMARY KEY,
    assessment_id NUMBER(19) NOT NULL,
    question_text VARCHAR2(1000) NOT NULL,
    points NUMBER(5) DEFAULT 10,
    CONSTRAINT fk_questions_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_questions START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 8. QUESTION OPTIONS TABLE & SEQUENCE
CREATE TABLE question_options (
    id NUMBER(19) PRIMARY KEY,
    question_id NUMBER(19) NOT NULL,
    option_text VARCHAR2(500) NOT NULL,
    is_correct NUMBER(1) DEFAULT 0 NOT NULL,
    CONSTRAINT fk_options_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_question_options START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 9. ENROLLMENTS TABLE & SEQUENCE
CREATE TABLE enrollments (
    id NUMBER(19) PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    course_id NUMBER(19) NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status VARCHAR2(20) DEFAULT 'ACTIVE' NOT NULL,
    completed_at TIMESTAMP,
    CONSTRAINT uk_user_course_enrollment UNIQUE (user_id, course_id),
    CONSTRAINT fk_enrollments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollments_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_enrollments START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 10. USER PROGRESS TABLE & SEQUENCE
CREATE TABLE user_progress (
    id NUMBER(19) PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    course_id NUMBER(19) NOT NULL,
    lesson_id NUMBER(19) NOT NULL,
    completed NUMBER(1) DEFAULT 0 NOT NULL,
    completed_at TIMESTAMP,
    CONSTRAINT uk_user_course_lesson UNIQUE (user_id, course_id, lesson_id),
    CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_progress_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_progress_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_user_progress START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 11. CERTIFICATES TABLE & SEQUENCE
CREATE TABLE certificates (
    id NUMBER(19) PRIMARY KEY,
    certificate_code VARCHAR2(50) NOT NULL UNIQUE,
    user_id NUMBER(19) NOT NULL,
    course_id NUMBER(19) NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_user_course_cert UNIQUE (user_id, course_id),
    CONSTRAINT fk_certificates_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_certificates_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_certificates START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 12. TEST ATTEMPTS TABLE & SEQUENCE
CREATE TABLE test_attempts (
    id NUMBER(19) PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    assessment_id NUMBER(19) NOT NULL,
    score NUMBER(5,2) NOT NULL,
    passed NUMBER(1) NOT NULL,
    total_questions NUMBER(5) NOT NULL,
    correct_answers NUMBER(5) NOT NULL,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_attempts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_attempts_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_test_attempts START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 13. COURSE REVIEWS TABLE & SEQUENCE
CREATE TABLE course_reviews (
    id NUMBER(19) PRIMARY KEY,
    course_id NUMBER(19) NOT NULL,
    user_id NUMBER(19) NOT NULL,
    rating NUMBER(2) NOT NULL,
    comment VARCHAR2(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_reviews_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_course_reviews START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 14. NOTIFICATIONS TABLE & SEQUENCE
CREATE TABLE notifications (
    id NUMBER(19) PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    message VARCHAR2(1000) NOT NULL,
    type VARCHAR2(30) DEFAULT 'SYSTEM',
    is_read NUMBER(1) DEFAULT 0 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_notifications START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- INDEXES FOR PERFORMANCE
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_courses_slug ON courses(slug);
CREATE INDEX idx_courses_category ON courses(category_id);
CREATE INDEX idx_enrollments_user ON enrollments(user_id);
CREATE INDEX idx_progress_user_course ON user_progress(user_id, course_id);
CREATE INDEX idx_certificates_code ON certificates(certificate_code);
