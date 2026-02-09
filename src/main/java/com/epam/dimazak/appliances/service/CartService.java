package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;

public interface CartService {
    void addToCart(String email, Long applianceId);
    CartDto getMyCart(String email);
    void clearCart(String email);
    void removeItem(String email, Long applianceId);
    void updateQuantity(String email, Long applianceId, Integer quantity);
    Long checkout(String email, CheckoutRequestDto request);
}