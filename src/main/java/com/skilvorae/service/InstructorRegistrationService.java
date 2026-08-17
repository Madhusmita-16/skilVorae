package com.skilvorae.service;

import com.skilvorae.entity.InstructorApplication;
import com.skilvorae.entity.User;
import com.skilvorae.enums.ApplicationStatus;
import com.skilvorae.enums.Role;
import com.skilvorae.repository.InstructorApplicationRepository;
import com.skilvorae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorRegistrationService {

    private final InstructorApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public InstructorApplication submitApplication(InstructorApplication application) {
        if (userRepository.findByEmail(application.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        return applicationRepository.save(application);
    }

    public List<InstructorApplication> getPendingApplications() {
        return applicationRepository.findByStatus(ApplicationStatus.PENDING);
    }

    @Transactional
    public void approveApplication(Long applicationId) {
        InstructorApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is already processed");
        }

        application.setStatus(ApplicationStatus.APPROVED);
        applicationRepository.save(application);

        String tempPassword = generateTempPassword();

        User instructor = User.builder()
                .fullName(application.getFullName())
                .email(application.getEmail())
                .phone(application.getPhone())
                .qualification(application.getQualification())
                .yearsOfExperience(application.getYearsOfExperience())
                .bio(application.getBio())
                .role(Role.INSTRUCTOR)
                .password(passwordEncoder.encode(tempPassword))
                .forcePasswordChange(true)
                .build();
        
        userRepository.save(instructor);
        sendApprovalEmail(instructor.getEmail(), tempPassword);
    }

    @Transactional
    public void rejectApplication(Long applicationId) {
        InstructorApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(application);
        // Optional: Send rejection email
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void sendApprovalEmail(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@skilvorae.com");
        message.setTo(toEmail);
        message.setSubject("Your Instructor Application has been Approved!");
        message.setText("Congratulations! Your application to become an instructor at SkilVorae has been approved.\n\n" +
                "Your temporary password is: " + tempPassword + "\n\n" +
                "Please log in and change your password immediately.");
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ". " + e.getMessage());
        }
    }
}
