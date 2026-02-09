package com.epam.dimazak.appliances.model.dto.order;

import com.epam.dimazak.appliances.model.DeliveryType;
import com.epam.dimazak.appliances.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderHistoryDto {
    private Long id;
    private LocalDateTime date;
    private BigDecimal totalAmount;
    private BigDecimal deliveryCost;
    private OrderStatus status;
    private DeliveryType deliveryType;
    private String deliveryAddress;
    private List<OrderHistoryItemDto> items;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
}