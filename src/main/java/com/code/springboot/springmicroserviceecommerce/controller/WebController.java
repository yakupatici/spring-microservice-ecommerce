package com.code.springboot.springmicroserviceecommerce.controller;

import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Controller to manage web pages and form operations
@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Display home page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Display dashboard page based on user role
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        // Check if user has ADMIN role
        if (authentication != null && 
            authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "dashboard"; // Admin dashboard
        } else {
            return "user-dashboard"; // Regular user dashboard
        }
    }
} 