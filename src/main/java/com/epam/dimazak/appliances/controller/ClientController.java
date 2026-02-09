package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.ClientFacade;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientFacade userFacade;

    @GetMapping
    public ResponseEntity<СlientProfileDto> getProfile(Authentication auth) {
        return ResponseEntity.ok(userFacade.getProfile(auth));
    }

    @PutMapping
    public ResponseEntity<СlientProfileDto> updateProfile(
            @RequestBody @Valid UpdateProfileRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(userFacade.updateProfile(request, auth));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            Authentication auth
    ) {
        userFacade.changePassword(request, auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderHistoryDto>> getMyOrders(
            @RequestParam(required = false) OrderStatus status,
            Authentication auth
    ) {
        return ResponseEntity.ok(userFacade.getUserOrders(status, auth));
    }

    @PatchMapping("/orders/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long id,
            Authentication auth
    ) {
        userFacade.cancelOrder(id, auth);
        return ResponseEntity.ok().build();
    }
}