package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import com.epam.dimazak.appliances.service.ClientService;
import com.epam.dimazak.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientService clientService;
    private final OrderService orderService;

    public СlientProfileDto getProfile(Authentication auth) {
        return clientService.getProfile(getUserEmail(auth));
    }

    public СlientProfileDto updateProfile(UpdateProfileRequest request, Authentication auth) {
        return clientService.updateProfile(getUserEmail(auth), request);
    }

    public void changePassword(ChangePasswordRequest request, Authentication auth) {
        clientService.changePassword(getUserEmail(auth), request);
    }

    public List<OrderHistoryDto> getUserOrders(OrderStatus status, Authentication auth) {
        return orderService.getUserOrders(getUserEmail(auth), status);
    }

    public void cancelOrder(Long orderId, Authentication auth) {
        orderService.cancelOrder(orderId, getUserEmail(auth));
    }


    private String getUserEmail(Authentication auth) {
        return auth.getName();
    }
}