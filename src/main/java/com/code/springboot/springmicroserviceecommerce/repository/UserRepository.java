package com.code.springboot.springmicroserviceecommerce.repository;

import com.code.springboot.springmicroserviceecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository for user database operations
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email
    Optional<User> findByEmail(String email);
    // Check if email is already registered  
    Boolean existsByEmail(String email);
}
