package com.epam.dimazak.appliances.model.dto.cart;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private Integer totalQuantity;

    private BigDecimal amountLeftForFreeShipping;
    private boolean isFreeShipping;
}