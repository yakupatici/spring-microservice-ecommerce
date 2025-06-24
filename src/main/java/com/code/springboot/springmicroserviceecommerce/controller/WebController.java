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

import java.util.List;

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
    public String home(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "index";
    }

    // Display dashboard page based on user role
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && 
            authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin/products";
        } else {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Add user information
            model.addAttribute("user", user);
            
            // Add cart count
            model.addAttribute("cartCount", cartService.getCartItemCount(user));
            
            // Add favorites count (placeholder until implemented)
            model.addAttribute("favoritesCount", 0);
            model.addAttribute("favoriteCount", 0);
            
            // Add total orders (placeholder)
            model.addAttribute("totalOrders", 0);
            
            // Add total spent (placeholder)
            model.addAttribute("totalSpent", 0.0);
            
            // Add recent orders (placeholder)
            model.addAttribute("recentOrders", List.of());
            
            // Add recent favorites (placeholder)
            model.addAttribute("recentFavorites", List.of());
            
            return "user-dashboard";
        }
    }

    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("cartItems", cartService.getCartItems(user));
        return "cart";
    }

    @GetMapping("/favorites")
    public String viewFavorites(Authentication authentication, Model model) {
        // TODO: Implement favorites functionality
        return "favorites";
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
            redirectAttributes.addFlashAttribute("success", "Ürün sepete eklendi!");
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
            redirectAttributes.addFlashAttribute("success", "Ürün sepetten kaldırıldı!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
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
            redirectAttributes.addFlashAttribute("success", "Sepet güncellendi!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "products";
    }
} 