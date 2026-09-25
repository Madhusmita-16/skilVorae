package com.skilvorae.service.impl;

import com.skilvorae.entity.InstructorApplication;
import com.skilvorae.entity.User;
import com.skilvorae.enums.ApplicationStatus;
import com.skilvorae.enums.Role;
import com.skilvorae.exception.BadRequestException;
import com.skilvorae.exception.ResourceNotFoundException;
import com.skilvorae.repository.InstructorApplicationRepository;
import com.skilvorae.repository.UserRepository;
import com.skilvorae.service.InstructorRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Enterprise implementation of InstructorRegistrationService for onboarding new instructors on SkilVorae.
 */
@Service
@Transactional
public class InstructorRegistrationServiceImpl implements InstructorRegistrationService {

    private final InstructorApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public InstructorRegistrationServiceImpl(InstructorApplicationRepository applicationRepository,
                                            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void submitApplication(Long userId, String bio, String expertise, String linkedinUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        InstructorApplication app = new InstructorApplication();
        app.setUser(user);
        app.setBio(bio);
        app.setExpertise(expertise);
        app.setLinkedinUrl(linkedinUrl);
        app.setStatus(ApplicationStatus.PENDING);
        app.setSubmittedAt(LocalDateTime.now());

        applicationRepository.save(app);
    }

    @Override
    public void approveApplication(Long applicationId) {
        InstructorApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("InstructorApplication", "id", applicationId));

        app.setStatus(ApplicationStatus.APPROVED);
        if (app.getUser() != null) {
            app.getUser().setRole(Role.INSTRUCTOR);
            userRepository.save(app.getUser());
        }
        applicationRepository.save(app);
    }

    @Override
    public void rejectApplication(Long applicationId, String reason) {
        InstructorApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("InstructorApplication", "id", applicationId));

        app.setStatus(ApplicationStatus.REJECTED);
        app.setRejectionReason(reason);
        applicationRepository.save(app);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorApplication> getPendingApplications() {
        return applicationRepository.findByStatus(ApplicationStatus.PENDING);
    }
}
