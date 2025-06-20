package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> findAllProducts();
    Product createProduct(Product product);
    Product createProductWithImage(Product product, MultipartFile image);
} 