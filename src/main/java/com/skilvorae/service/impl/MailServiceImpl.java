package com.skilvorae.service.impl;

import com.skilvorae.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Enterprise implementation of MailService for SkilVorae transactional emails.
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Autowired(required = false)
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otpCode) {
        if (mailSender == null) return;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@skilvorae.com");
            message.setTo(toEmail);
            message.setSubject("SkilVorae - Verification OTP Code");
            message.setText("Your verification OTP code is: " + otpCode + ". Valid for 10 minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            // Log mail dispatch exception
        }
    }

    @Override
    public void sendCourseEnrollmentConfirmation(String toEmail, String courseTitle) {
        if (mailSender == null) return;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@skilvorae.com");
            message.setTo(toEmail);
            message.setSubject("Enrollment Confirmed: " + courseTitle);
            message.setText("Congratulations! You are now enrolled in " + courseTitle + ". Happy learning!");
            mailSender.send(message);
        } catch (Exception e) {
            // Log mail dispatch exception
        }
    }
}
