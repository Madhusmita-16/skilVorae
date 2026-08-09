package com.skilvorae.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "SkilVorae - Password Reset OTP";
        String content = String.format(
            "Hello,\n\nYour OTP for resetting your SkilVorae password is: %s\n\nThis OTP is valid for 10 minutes. Do not share it with anyone.\n\nBest regards,\nSkilVorae Team",
            otp
        );

        // Always log OTP to stdout for instant dev testing
        log.info("==================================================");
        log.info("MOCK MAIL SENDER - OTP FOR {}: {}", toEmail, otp);
        log.info("==================================================");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@skilvorae.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Real SMTP mail send failed (falling back to mock console output): {}", e.getMessage());
        }
    }
}
