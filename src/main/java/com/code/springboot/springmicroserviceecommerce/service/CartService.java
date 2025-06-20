package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.model.CartItem;
import com.code.springboot.springmicroserviceecommerce.model.User;

import java.util.List;

public interface CartService {
    CartItem addToCart(User user, Long productId, Integer quantity);
    List<CartItem> getCartItems(User user);
    void removeFromCart(User user, Long productId);
    void updateCartItemQuantity(User user, Long productId, Integer quantity);
} 