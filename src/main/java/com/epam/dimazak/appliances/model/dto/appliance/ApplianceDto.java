package com.epam.dimazak.appliances.model.dto.appliance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplianceDto {
    private Long id;

    private String nameEn;
    private String nameUa;

    private String descriptionEn;
    private String descriptionUa;

    private BigDecimal price;
    private String imageUrl;
    private boolean active;
    private Integer stockQuantity;

    private String categoryNameEn;
    private String categoryNameUa;
    private Long categoryId;

    private String manufacturerName;
    private Long manufacturerId;
}