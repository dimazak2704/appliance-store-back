package com.epam.dimazak.appliances.model.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderHistoryItemDto {
    private Long productId;
    private String productNameEn;
    private String productNameUa;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal rowTotal;
}