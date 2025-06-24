package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.model.CartItem;
import com.code.springboot.springmicroserviceecommerce.model.Product;
import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.repository.CartItemRepository;
import com.code.springboot.springmicroserviceecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartItem addToCart(User user, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        CartItem cartItem = cartItemRepository.findByUserAndProduct_Id(user, productId)
                .orElse(new CartItem(null, user, product, 0));

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void removeFromCart(User user, Long productId) {
        cartItemRepository.findByUserAndProduct_Id(user, productId)
                .ifPresent(cartItemRepository::delete);
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(User user, Long productId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findByUserAndProduct_Id(user, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return;
        }

        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    public int getCartItemCount(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return cartItems.size();
    }
} 