package com.epam.dimazak.appliances.model.dto.appliance;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApplianceFilterDto {
    private Long categoryId;
    private Long manufacturerId;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}