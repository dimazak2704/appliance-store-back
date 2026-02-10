package com.epam.dimazak.appliances.controller.admin;

import com.epam.dimazak.appliances.facade.admin.AdminOrderFacade;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrderControllerTest {

    @Mock
    private AdminOrderFacade adminOrderFacade;

    private AdminOrderController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminOrderController(adminOrderFacade);
    }

    @Test
    void getAllOrders_shouldReturnOkAndPageOfOrders() {
        OrderHistoryDto orderDto = OrderHistoryDto.builder().id(1L).build();
        Page<OrderHistoryDto> page = new PageImpl<>(List.of(orderDto));

        when(adminOrderFacade.getAllOrders(any(OrderFilterDto.class), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<OrderHistoryDto>> response = controller.getAllOrders(null, null, null, Pageable.unpaged());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(adminOrderFacade).getAllOrders(any(OrderFilterDto.class), any(Pageable.class));
    }

    @Test
    void updateOrderStatus_shouldReturnOk() {
        Long id = 1L;
        OrderStatus newStatus = OrderStatus.COMPLETED;

        ResponseEntity<Void> response = controller.updateOrderStatus(id, newStatus);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(adminOrderFacade).updateOrderStatus(id, newStatus);
    }
}
