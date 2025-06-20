package com.code.springboot.springmicroserviceecommerce.controller;

import com.code.springboot.springmicroserviceecommerce.service.ProductService;
import com.code.springboot.springmicroserviceecommerce.service.IExternalProductService;
import com.code.springboot.springmicroserviceecommerce.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.code.springboot.springmicroserviceecommerce.model.Product;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final ProductService productService;
    private final IExternalProductService externalProductService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("product", new Product()); // For the form
        return "admin/products"; // This will look for a template at templates/admin/products.html
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product, 
                           @RequestParam(value = "image", required = false) MultipartFile image,
                           RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                productService.createProductWithImage(product, image);
            } else {
                productService.createProduct(product);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        } catch (Exception e) {
            log.error("Error adding product", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/import")
    public String importProducts(RedirectAttributes redirectAttributes) {
        try {
            List<Product> importedProducts = externalProductService.fetchProductsFromExternalApi();
            for (Product product : importedProducts) {
                productService.createProduct(product);
            }
            redirectAttributes.addFlashAttribute("successMessage", 
                String.format("Successfully imported %d products!", importedProducts.size()));
        } catch (ExternalApiException e) {
            log.error("Error importing products", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to import products: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }
} 