package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.ClientFacade;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientFacade clientFacade;

    private ClientController controller;

    @BeforeEach
    void setUp() {
        controller = new ClientController(clientFacade);
    }

    @Test
    void getProfile_shouldReturnOkAndClientProfileDto() {
        Authentication auth = mock(Authentication.class);
        СlientProfileDto profileDto = СlientProfileDto.builder().email("test@mail.com").build();

        when(clientFacade.getProfile(auth)).thenReturn(profileDto);

        ResponseEntity<СlientProfileDto> response = controller.getProfile(auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(profileDto);
        verify(clientFacade).getProfile(auth);
    }

    @Test
    void updateProfile_shouldReturnOkAndUpdatedProfile() {
        Authentication auth = mock(Authentication.class);
        UpdateProfileRequest request = new UpdateProfileRequest();
        СlientProfileDto profileDto = СlientProfileDto.builder().email("updated@mail.com").build();

        when(clientFacade.updateProfile(any(UpdateProfileRequest.class), eq(auth))).thenReturn(profileDto);

        ResponseEntity<СlientProfileDto> response = controller.updateProfile(request, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(profileDto);
        verify(clientFacade).updateProfile(request, auth);
    }

    @Test
    void changePassword_shouldReturnOk() {
        Authentication auth = mock(Authentication.class);
        ChangePasswordRequest request = new ChangePasswordRequest();

        ResponseEntity<Void> response = controller.changePassword(request, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(clientFacade).changePassword(request, auth);
    }

    @Test
    void getMyOrders_shouldReturnOkAndListOfOrders() {
        Authentication auth = mock(Authentication.class);
        OrderStatus status = OrderStatus.NEW;
        OrderHistoryDto orderDto = OrderHistoryDto.builder().id(1L).build();
        List<OrderHistoryDto> orders = List.of(orderDto);

        when(clientFacade.getUserOrders(status, auth)).thenReturn(orders);

        ResponseEntity<List<OrderHistoryDto>> response = controller.getMyOrders(status, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
        verify(clientFacade).getUserOrders(status, auth);
    }

    @Test
    void cancelOrder_shouldReturnOk() {
        Long id = 1L;
        Authentication auth = mock(Authentication.class);

        ResponseEntity<Void> response = controller.cancelOrder(id, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(clientFacade).cancelOrder(id, auth);
    }
}
