package com.code.springboot.springmicroserviceecommerce.repository;

import com.code.springboot.springmicroserviceecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
} 