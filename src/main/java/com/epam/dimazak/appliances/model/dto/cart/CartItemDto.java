package com.epam.dimazak.appliances.model.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemDto {
    private Long id;
    private String nameEn;
    private String nameUa;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal rowTotal;
}