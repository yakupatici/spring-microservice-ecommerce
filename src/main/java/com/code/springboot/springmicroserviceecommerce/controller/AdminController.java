package com.code.springboot.springmicroserviceecommerce.controller;

import com.code.springboot.springmicroserviceecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.code.springboot.springmicroserviceecommerce.model.Product;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("product", new Product()); // For the form
        return "admin/products"; // This will look for a template at templates/admin/products.html
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.createProduct(product);
        return "redirect:/admin/products";
    }
} 