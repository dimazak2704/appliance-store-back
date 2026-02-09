package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.UserProfileDto;
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

    public UserProfileDto getProfile(Authentication auth) {
        return clientService.getProfile(auth.getName());
    }

    public UserProfileDto updateProfile(UpdateProfileRequest request, Authentication auth) {
        return clientService.updateProfile(auth.getName(), request);
    }

    public void changePassword(ChangePasswordRequest request, Authentication auth) {
        clientService.changePassword(auth.getName(), request);
    }

    public List<OrderHistoryDto> getUserOrders(OrderStatus status, Authentication auth) {
        return orderService.getUserOrders(auth.getName(), status);
    }
}