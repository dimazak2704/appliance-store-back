package com.epam.dimazak.appliances.facade.admin;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminOrderFacadeTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private AdminOrderFacade adminOrderFacade;

    @Test
    void getAllOrders_shouldReturnPageOfOrders() {
        OrderFilterDto filter = new OrderFilterDto();
        Pageable pageable = Pageable.unpaged();
        Page<OrderHistoryDto> expectedPage = new PageImpl<>(Collections.emptyList());
        when(orderService.getAllOrders(filter, pageable)).thenReturn(expectedPage);

        Page<OrderHistoryDto> result = adminOrderFacade.getAllOrders(filter, pageable);

        assertThat(result).isEqualTo(expectedPage);
        verify(orderService).getAllOrders(filter, pageable);
    }

    @Test
    void updateOrderStatus_shouldCallService() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.CONFIRMED;

        adminOrderFacade.updateOrderStatus(orderId, newStatus);

        verify(orderService).updateOrderStatus(orderId, newStatus);
    }
}
