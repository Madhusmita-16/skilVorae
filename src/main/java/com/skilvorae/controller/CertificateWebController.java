package com.skilvorae.controller;

import com.skilvorae.dto.CertificateDto;
import com.skilvorae.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CertificateWebController {

    private final CertificateService certificateService;

    @GetMapping("/certificates/{code}")
    public String viewCertificate(@PathVariable String code, Model model) {
        CertificateDto certificate = certificateService.getCertificateByCode(code);
        model.addAttribute("certificate", certificate);
        return "certificate/view";
    }
}
