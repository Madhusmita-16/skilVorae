package com.skilvorae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StubWebController {

    @RequestMapping(value = {
        "/assessments/**",
        "/events/**",
        "/deadlines/**",
        "/certificates/**",
        "/admin/revenue",
        "/admin/events",
        "/admin/pending",
        "/instructor/calendar",
        "/instructor/analytics",
        "/instructor/earnings",
        "/instructor/pending",
        "/calendar",
        "/notifications",
        "/settings"
    })
    public String handleStubRoutes(HttpServletRequest request, Model model) {
        model.addAttribute("path", request.getRequestURI());
        return "stub";
    }
}
