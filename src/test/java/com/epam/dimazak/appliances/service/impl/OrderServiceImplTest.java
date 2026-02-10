package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.OrderLogicException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.exception.UserAccessDeniedException;
import com.epam.dimazak.appliances.model.*;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.repository.ApplianceRepository;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getUserOrders_returnsHistoryList() {
        String email = "user@example.com";
        Client client = new Client();
        client.setEmail(email);

        Orders order = new Orders();
        order.setId(1L);
        order.setClient(client);
        order.setTotalAmount(BigDecimal.TEN);
        order.setOrderRowSet(new HashSet<>());

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(ordersRepository.findAllByClientOrderByCreatedAtDesc(client)).thenReturn(Arrays.asList(order));

        List<OrderHistoryDto> result = orderService.getUserOrders(email, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void updateOrderStatus_updatesAndSendsEmail() {
        Long orderId = 1L;
        Client client = new Client();
        client.setEmail("client@example.com");

        Orders order = new Orders();
        order.setId(orderId);
        order.setClient(client);
        order.setStatus(OrderStatus.NEW);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        verify(ordersRepository).save(order);
        verify(emailService).sendOrderStatusChangeNotification(anyString(), eq(orderId), eq(OrderStatus.CONFIRMED));
    }

    @Test
    void cancelOrder_whenValidNewOrder_cancelsAndRestoresStock() {
        String email = "user@example.com";
        Long orderId = 1L;

        Client client = new Client();
        client.setEmail(email);

        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setStockQuantity(5);

        OrderRow row = new OrderRow();
        row.setAppliance(appliance);
        row.setNumber(3L);

        Orders order = new Orders();
        order.setId(orderId);
        order.setClient(client);
        order.setStatus(OrderStatus.NEW);
        order.setOrderRowSet(new HashSet<>(Arrays.asList(row)));

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId, email);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        assertThat(appliance.getStockQuantity()).isEqualTo(8);
        verify(applianceRepository).save(appliance);
        verify(ordersRepository).save(order);
    }

    @Test
    void cancelOrder_whenDifferentUser_throwsAccessDenied() {
        String email = "user@example.com";
        Long orderId = 1L;

        Client client = new Client();
        client.setEmail("other@example.com");

        Orders order = new Orders();
        order.setId(orderId);
        order.setClient(client);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Access denied");

        assertThatThrownBy(() -> orderService.cancelOrder(orderId, email))
                .isInstanceOf(UserAccessDeniedException.class);
    }

    @Test
    void cancelOrder_whenNotNewStatus_throwsOrderLogicException() {
        String email = "user@example.com";
        Long orderId = 1L;

        Client client = new Client();
        client.setEmail(email);

        Orders order = new Orders();
        order.setId(orderId);
        order.setClient(client);
        order.setStatus(OrderStatus.CONFIRMED);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cannot cancel");

        assertThatThrownBy(() -> orderService.cancelOrder(orderId, email))
                .isInstanceOf(OrderLogicException.class);
    }
}
