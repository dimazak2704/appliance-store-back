package com.epam.dimazak.appliances.facade.admin;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminOrderFacade {

    private final OrderService orderService;

    public Page<OrderHistoryDto> getAllOrders(OrderFilterDto filter, Pageable pageable) {
        return orderService.getAllOrders(filter, pageable);
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
    }
}