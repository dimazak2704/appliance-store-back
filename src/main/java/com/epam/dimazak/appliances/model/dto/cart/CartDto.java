package com.epam.dimazak.appliances.model.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartDto {
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private Integer totalQuantity;

    private BigDecimal amountLeftForFreeShipping;
    private boolean isFreeShipping;
}