# Product Requirements Document (PRD)

## Product
**SkilVorae** — Enterprise Learning Management System (LMS), PDF Syllabus Parser & Certificate Engine

## Problem
Educational institutions need automated tools to ingest course curriculum PDFs, generate online quizzes, track student completions, and render official completion certificates without manual admin overhead.

## Target Users
- Students, Course Instructors, and Academic Administrators.

## Core Features
1. **Course Catalog & Video Player**: Lesson player with progress tracking.
2. **Automated PDF Curriculum Parser**: Background service extracting course structures and quiz questions from uploaded PDF files (`PdfParsingService.java`).
3. **Quiz & Assessment Engine**: Automated multiple-choice evaluation and score calculation.
4. **Dynamic PDF Certificate Generator**: Renders personalized completion certificates upon course passing (`CertificateService.java`).
5. **Polyglot Database Support**: Oracle 19c/21c (`schema-oracle.sql`), PostgreSQL, MySQL, and Docker Compose DB containers.

## Out of Scope
- Native mobile app.
