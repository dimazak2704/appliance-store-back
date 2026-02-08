package com.epam.dimazak.appliances.model.dto.appliance;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ApplianceFilterDto {
    private Long categoryId;
    private List<Long> manufacturerIds;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private List<String> powerTypes;

    private Integer minPower;
    private Integer maxPower;
}