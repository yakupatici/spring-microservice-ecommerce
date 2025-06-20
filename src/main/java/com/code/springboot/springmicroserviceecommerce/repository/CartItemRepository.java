package com.code.springboot.springmicroserviceecommerce.repository;

import com.code.springboot.springmicroserviceecommerce.model.CartItem;
import com.code.springboot.springmicroserviceecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct_Id(User user, Long productId);
} 