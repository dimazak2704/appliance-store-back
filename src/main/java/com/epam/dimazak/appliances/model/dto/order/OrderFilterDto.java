package com.epam.dimazak.appliances.model.dto.order;

import lombok.Data;

@Data
public class OrderFilterDto {
    private String search;
    private String status;
    private String deliveryType;
}