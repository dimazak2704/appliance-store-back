package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import com.epam.dimazak.appliances.service.ClientService;
import com.epam.dimazak.appliances.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientFacadeTest {

    @Mock
    private ClientService clientService;

    @Mock
    private OrderService orderService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ClientFacade clientFacade;

    private static final String EMAIL = "test@example.com";

    @Test
    void getProfile_shouldReturnProfileDto() {
        СlientProfileDto expectedProfile = new СlientProfileDto();
        when(authentication.getName()).thenReturn(EMAIL);
        when(clientService.getProfile(EMAIL)).thenReturn(expectedProfile);

        СlientProfileDto result = clientFacade.getProfile(authentication);

        assertThat(result).isEqualTo(expectedProfile);
        verify(clientService).getProfile(EMAIL);
    }

    @Test
    void updateProfile_shouldReturnUpdatedProfile() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        СlientProfileDto expectedProfile = new СlientProfileDto();
        when(authentication.getName()).thenReturn(EMAIL);
        when(clientService.updateProfile(EMAIL, request)).thenReturn(expectedProfile);

        СlientProfileDto result = clientFacade.updateProfile(request, authentication);

        assertThat(result).isEqualTo(expectedProfile);
        verify(clientService).updateProfile(EMAIL, request);
    }

    @Test
    void changePassword_shouldCallService() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        when(authentication.getName()).thenReturn(EMAIL);

        clientFacade.changePassword(request, authentication);

        verify(clientService).changePassword(EMAIL, request);
    }

    @Test
    void getUserOrders_shouldReturnOrderHistory() {
        OrderStatus status = OrderStatus.CONFIRMED;
        List<OrderHistoryDto> expectedOrders = Collections.emptyList();
        when(authentication.getName()).thenReturn(EMAIL);
        when(orderService.getUserOrders(EMAIL, status)).thenReturn(expectedOrders);

        List<OrderHistoryDto> result = clientFacade.getUserOrders(status, authentication);

        assertThat(result).isEqualTo(expectedOrders);
        verify(orderService).getUserOrders(EMAIL, status);
    }

    @Test
    void cancelOrder_shouldCallService() {
        Long orderId = 1L;
        when(authentication.getName()).thenReturn(EMAIL);

        clientFacade.cancelOrder(orderId, authentication);

        verify(orderService).cancelOrder(orderId, EMAIL);
    }
}
