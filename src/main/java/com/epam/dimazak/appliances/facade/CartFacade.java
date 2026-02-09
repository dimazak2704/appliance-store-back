package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;
import com.epam.dimazak.appliances.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartFacade {

    private final CartService cartService;

    public void addToCart(Long applianceId, Authentication auth) {
        cartService.addToCart(getUserEmail(auth), applianceId);
    }

    public CartDto getMyCart(Authentication auth) {
        return cartService.getMyCart(getUserEmail(auth));
    }

    public void removeItem(Long applianceId, Authentication auth) {
        cartService.removeItem(getUserEmail(auth), applianceId);
    }

    public void updateQuantity(Long applianceId, Integer quantity, Authentication auth) {
        cartService.updateQuantity(getUserEmail(auth), applianceId, quantity);
    }

    public Long checkout(CheckoutRequestDto request, Authentication auth) {
        return cartService.checkout(getUserEmail(auth), request);
    }

    private String getUserEmail(Authentication auth) {
        return auth.getName();
    }
}