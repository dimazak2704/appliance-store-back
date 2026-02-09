package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<OrderHistoryDto> getUserOrders(String userEmail, OrderStatus status);
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    void cancelOrder(Long orderId, String userEmail);
    Page<OrderHistoryDto> getAllOrders(OrderFilterDto filter, Pageable pageable);
}