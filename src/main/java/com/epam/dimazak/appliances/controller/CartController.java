package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.CartFacade;
import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartFacade cartFacade;

    @PostMapping("/add/{applianceId}")
    public ResponseEntity<Void> addToCart(@PathVariable Long applianceId, Authentication auth) {
        cartFacade.addToCart(applianceId, auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication auth) {
        return ResponseEntity.ok(cartFacade.getMyCart(auth));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Long> checkout(@RequestBody @Valid CheckoutRequestDto request, Authentication auth) {
        Long orderId = cartFacade.checkout(request, auth);
        return ResponseEntity.ok(orderId);
    }

    @DeleteMapping("/remove/{applianceId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long applianceId, Authentication auth) {
        cartFacade.removeItem(applianceId, auth);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{applianceId}")
    public ResponseEntity<Void> updateQuantity(
            @PathVariable Long applianceId,
            @RequestParam Integer quantity,
            Authentication auth
    ) {
        cartFacade.updateQuantity(applianceId, quantity, auth);
        return ResponseEntity.ok().build();
    }


}