package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.Orders;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryItemDto;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.OrdersRepository;
import com.epam.dimazak.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final ClientRepository clientRepository;
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<OrderHistoryDto> getUserOrders(String userEmail, OrderStatus status) {
        Client client = clientRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));

        List<Orders> orders;
        if (status != null) {
            orders = ordersRepository.findAllByClientAndStatusOrderByCreatedAtDesc(client, status);
        } else {
            orders = ordersRepository.findAllByClientOrderByCreatedAtDesc(client);
        }

        return orders.stream()
                .map(this::mapToHistoryDto)
                .collect(Collectors.toList());
    }

    private OrderHistoryDto mapToHistoryDto(Orders order) {
        List<OrderHistoryItemDto> items = order.getOrderRowSet().stream()
                .map(row -> OrderHistoryItemDto.builder()
                        .productId(row.getAppliance().getId())
                        .productNameEn(row.getAppliance().getNameEn())
                        .productNameUa(row.getAppliance().getNameUa())
                        .imageUrl(row.getAppliance().getImageUrl())
                        .quantity(row.getNumber().intValue())
                        .priceAtPurchase(row.getAppliance().getPrice())
                        .rowTotal(row.getAmount())
                        .build())
                .collect(Collectors.toList());

        return OrderHistoryDto.builder()
                .id(order.getId())
                .date(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .deliveryCost(order.getDeliveryCost())
                .status(order.getStatus())
                .deliveryType(order.getDeliveryType())
                .deliveryAddress(order.getDeliveryAddress())
                .items(items)
                .build();
    }
}