package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.CartEmptyException;
import com.epam.dimazak.appliances.exception.NotEnoughStockException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.*;
import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CartItemDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;
import com.epam.dimazak.appliances.repository.*;
import com.epam.dimazak.appliances.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final ApplianceRepository applianceRepository;
    private final OrdersRepository orderRepository;
    private final MessageSource messageSource;

    private static final BigDecimal FREE_SHIPPING_THRESHOLD = BigDecimal.valueOf(30000);

    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    @Loggable
    public void addToCart(String email, Long applianceId) {
        Client client = getClient(email);
        Cart cart = getOrCreateCart(client);
        Appliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.appliance.not_found") + " id: " + applianceId));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getAppliance().getId().equals(applianceId))
                .findFirst();

        int newQuantity = 1;

        if (existingItem.isPresent()) {
            newQuantity = existingItem.get().getQuantity() + 1;
        }

        if (appliance.getStockQuantity() < newQuantity) {
            String name = LocaleContextHolder.getLocale().getLanguage().equals("uk")
                    ? appliance.getNameUa() : appliance.getNameEn();
            throw new NotEnoughStockException(getMsg("error.stock.not_enough", name, appliance.getStockQuantity()));
        }
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .appliance(appliance)
                    .quantity(1)
                    .build();
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    @Loggable
    public CartDto getMyCart(String email) {
        Client client = getClient(email);
        Cart cart = getOrCreateCart(client);
        return mapToCartDto(cart);
    }

    @Override
    @Transactional
    @Loggable
    public void clearCart(String email) {
        Client client = getClient(email);
        Cart cart = getOrCreateCart(client);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    @Loggable
    public Long checkout(String email, CheckoutRequestDto request) {
        Client client = getClient(email);
        Cart cart = getOrCreateCart(client);

        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException(getMsg("error.cart.empty"));
        }

        if (client.getCard() == null || client.getCard().trim().isEmpty()) {
            throw new BusinessRuleException(getMsg("error.card.required"));
        }

        boolean isDelivery = request.getDeliveryType() == DeliveryType.COURIER || request.getDeliveryType() == DeliveryType.POST;
        if (isDelivery && (request.getAddress() == null || request.getAddress().isBlank())) {
            throw new BusinessRuleException(getMsg("validation.address.required"));
        }

        Orders order = Orders.builder()
                .client(client)
                .status(OrderStatus.NEW)
                .deliveryType(request.getDeliveryType())
                .deliveryAddress(request.getAddress())
                .contactPhone(request.getPhone())
                .build();

        BigDecimal goodsTotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Appliance appliance = cartItem.getAppliance();
            int qty = cartItem.getQuantity();

            if (appliance.getStockQuantity() < qty) {
                String name = LocaleContextHolder.getLocale().getLanguage().equals("uk")
                        ? appliance.getNameUa() : appliance.getNameEn();
                throw new NotEnoughStockException(getMsg("error.stock.not_enough", name, appliance.getStockQuantity()));
            }

            appliance.setStockQuantity(appliance.getStockQuantity() - qty);
            applianceRepository.save(appliance);

            BigDecimal rowTotal = appliance.getPrice().multiply(BigDecimal.valueOf(qty));

            OrderRow row = OrderRow.builder()
                    .appliance(appliance)
                    .number((long) qty)
                    .amount(rowTotal)
                    .build();

            order.addRow(row);
            goodsTotal = goodsTotal.add(rowTotal);
        }

        BigDecimal deliveryCost = BigDecimal.ZERO;

        boolean isFreeShipping = goodsTotal.compareTo(FREE_SHIPPING_THRESHOLD) > 0;

        if (!isFreeShipping) {
            if (request.getDeliveryType() == DeliveryType.COURIER) {
                deliveryCost = BigDecimal.valueOf(200);
            } else if (request.getDeliveryType() == DeliveryType.POST) {
                deliveryCost = BigDecimal.valueOf(100);
            }
        }

        order.setGoodsTotalAmount(goodsTotal);
        order.setDeliveryCost(deliveryCost);
        order.setTotalAmount(goodsTotal.add(deliveryCost));

        Orders savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder.getId();
    }

    @Override
    @Transactional
    @Loggable
    public void removeItem(String email, Long applianceId) {
        Client client = getClient(email);
        Cart cart = getOrCreateCart(client);
        boolean removed = cart.getItems().removeIf(item -> item.getAppliance().getId().equals(applianceId));

        if (removed) {
            cartRepository.save(cart);
        } else {
            throw new ResourceNotFoundException(getMsg("error.item.not_found"));
        }
    }

    @Override
    @Transactional
    @Loggable
    public void updateQuantity(String email, Long applianceId, Integer quantity) {
        if (quantity <= 0) {
            removeItem(email, applianceId);
            return;
        }
        Client client = getClient(email);
        Cart cart = getOrCreateCart(client);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getAppliance().getId().equals(applianceId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.item.not_found")));

        if (item.getAppliance().getStockQuantity() < quantity) {
            String name = LocaleContextHolder.getLocale().getLanguage().equals("uk")
                    ? item.getAppliance().getNameUa() : item.getAppliance().getNameEn();
            throw new NotEnoughStockException(getMsg("error.stock.not_enough", name, item.getAppliance().getStockQuantity()));
        }
        item.setQuantity(quantity);
        cartRepository.save(cart);
    }

    private Client getClient(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
    }

    private Cart getOrCreateCart(Client client) {
        return cartRepository.findByClient(client)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setClient(client);
                    return cartRepository.save(newCart);
                });
    }

    private CartDto mapToCartDto(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(item -> {
                    BigDecimal rowTotal = item.getAppliance().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

                    return CartItemDto.builder()
                            .id(item.getAppliance().getId())
                            .nameEn(item.getAppliance().getNameEn())
                            .nameUa(item.getAppliance().getNameUa())
                            .imageUrl(item.getAppliance().getImageUrl())
                            .price(item.getAppliance().getPrice())
                            .quantity(item.getQuantity())
                            .rowTotal(rowTotal)
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal totalPrice = itemDtos.stream()
                .map(CartItemDto::getRowTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalQuantity = itemDtos.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();

        BigDecimal amountLeft = FREE_SHIPPING_THRESHOLD.subtract(totalPrice).max(BigDecimal.ZERO);
        boolean isFree = totalPrice.compareTo(FREE_SHIPPING_THRESHOLD) > 0;

        return CartDto.builder()
                .items(itemDtos)
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .amountLeftForFreeShipping(amountLeft)
                .isFreeShipping(isFree)
                .build();
    }
}