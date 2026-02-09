package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import java.util.List;

public interface OrderService {
    List<OrderHistoryDto> getUserOrders(String userEmail, OrderStatus status);
}