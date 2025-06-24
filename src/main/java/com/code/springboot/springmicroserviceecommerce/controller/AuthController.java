package com.code.springboot.springmicroserviceecommerce.controller;

import com.code.springboot.springmicroserviceecommerce.dto.UserDto;
import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.service.ProductService;
import com.code.springboot.springmicroserviceecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    private final UserService userService;
    private final ProductService productService;

    public AuthController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("products", productService.findAllProducts());
        return "register";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                             BindingResult result,
                             Model model) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "Bu e-posta adresi ile kayıtlı bir hesap zaten var");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("products", productService.findAllProducts());
            return "register";
        }
        
        userService.saveUser(user);
        return "redirect:/login?registered";
    }
} 