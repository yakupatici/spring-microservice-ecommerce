package com.code.springboot.springmicroserviceecommerce.controller;

import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userRepository.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email is already registered!");
            return "redirect:/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        redirectAttributes.addFlashAttribute("success", "Registration successful! You can now sign in.");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, 
                           @RequestParam String password, 
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (passwordEncoder.matches(password, user.getPassword())) {
                redirectAttributes.addFlashAttribute("success", "Login successful!");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid password!");
                return "redirect:/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "User not found!");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
} 