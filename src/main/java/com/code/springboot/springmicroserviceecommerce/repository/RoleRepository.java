package com.code.springboot.springmicroserviceecommerce.repository;

import com.code.springboot.springmicroserviceecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
} 