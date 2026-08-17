package com.skilvorae.controller;

import com.skilvorae.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@lombok.RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
                            Model model) {
        if (userDetails != null) {
            boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isInstructor = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"));
            if (isAdmin) return "redirect:/admin/dashboard";
            if (isInstructor) return "redirect:/instructor/dashboard";
            return "redirect:/dashboard";
        }
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        if (userDetails != null) {
            boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isInstructor = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"));
            if (isAdmin) return "redirect:/admin/dashboard";
            if (isInstructor) return "redirect:/instructor/dashboard";
            return "redirect:/dashboard";
        }
        return "auth/register";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam(required = false) String email, Model model) {
        model.addAttribute("email", email);
        return "auth/verify-otp";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(required = false) String email,
                                    @RequestParam(required = false) String otp,
                                    Model model) {
        model.addAttribute("email", email);
        model.addAttribute("otp", otp);
        return "auth/reset-password";
    }

    @GetMapping("/auth/force-password-change")
    public String forcePasswordChangePage(@org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";
        return "auth/force-password-change";
    }

    @PostMapping("/auth/update-forced-password")
    public String updateForcedPassword(
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            Model model) {
            
        if (userDetails == null) return "redirect:/login";
        
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "auth/force-password-change";
        }
        com.skilvorae.entity.User dbUser = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (dbUser != null) {
            dbUser.setPassword(passwordEncoder.encode(newPassword));
            dbUser.setForcePasswordChange(false);
            userRepository.save(dbUser);
        }
        
        return "redirect:/dashboard"; 
    }
}
