package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.OrderLogicException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.exception.UserAccessDeniedException;
import com.epam.dimazak.appliances.model.*;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryItemDto;
import com.epam.dimazak.appliances.repository.ApplianceRepository;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.OrdersRepository;
import com.epam.dimazak.appliances.repository.specification.OrderSpecification;
import com.epam.dimazak.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final ClientRepository clientRepository;
    private final ApplianceRepository applianceRepository;
    private final EmailService emailService;
    private final MessageSource messageSource;

    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
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

    @Override
    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getAllOrders(OrderFilterDto filter, Pageable pageable) {
        return ordersRepository.findAll(OrderSpecification.getSpecifications(filter), pageable)
                .map(this::mapToHistoryDto);
    }

    @Override
    @Transactional
    @Loggable
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.order.not_found")));

        order.setStatus(newStatus);
        ordersRepository.save(order);
        try {
            emailService.sendOrderStatusChangeNotification(
                    order.getClient().getEmail(),
                    order.getId(),
                    newStatus
            );
        } catch (Exception e) {
            System.err.println("Failed to send notification email: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @Loggable
    public void cancelOrder(Long orderId, String userEmail) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.order.not_found")));

        if (!order.getClient().getEmail().equals(userEmail)) {
            throw new UserAccessDeniedException(getMsg("error.order.access_denied"));
        }

        if (order.getStatus() != OrderStatus.NEW) {
            throw new OrderLogicException(getMsg("error.order.cancel_status", order.getStatus()));
        }

        for (OrderRow row : order.getOrderRowSet()) {
            Appliance appliance = row.getAppliance();
            appliance.setStockQuantity(appliance.getStockQuantity() + row.getNumber().intValue());
            applianceRepository.save(appliance);
        }

        order.setStatus(OrderStatus.CANCELED);
        ordersRepository.save(order);
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
                .clientName(order.getClient().getName())
                .clientEmail(order.getClient().getEmail())
                .clientPhone(order.getContactPhone())
                .build();
    }
}