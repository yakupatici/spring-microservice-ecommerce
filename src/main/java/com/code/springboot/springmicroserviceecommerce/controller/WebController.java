package com.code.springboot.springmicroserviceecommerce.controller;

import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.repository.UserRepository;
import com.code.springboot.springmicroserviceecommerce.service.CartService;
import com.code.springboot.springmicroserviceecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller to manage web pages and form operations
@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final CartService cartService;

    // Display home page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Display dashboard page based on user role
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Check if user has ADMIN role
        if (authentication != null && 
            authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "dashboard"; // Admin dashboard
        } else {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("products", productService.findAllProducts());
            model.addAttribute("cartItems", cartService.getCartItems(user));
            return "user-dashboard"; // Regular user dashboard
        }
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId, 
                          @RequestParam(defaultValue = "1") Integer quantity,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartService.addToCart(user, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Product added to cart successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartService.removeFromCart(user, productId);
            redirectAttributes.addFlashAttribute("success", "Product removed from cart!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/cart/update/{productId}")
    public String updateCartItemQuantity(@PathVariable Long productId,
                                       @RequestParam Integer quantity,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartService.updateCartItemQuantity(user, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Cart updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }
} 