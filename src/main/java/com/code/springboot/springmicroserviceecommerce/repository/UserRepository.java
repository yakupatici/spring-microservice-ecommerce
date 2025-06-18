package com.code.springboot.springmicroserviceecommerce.repository;

import com.code.springboot.springmicroserviceecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
} 