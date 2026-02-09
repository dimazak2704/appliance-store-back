package com.epam.dimazak.appliances.model.dto.appliance;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplianceRequestDto {
    @NotBlank(message = "{validation.appliance.nameEn.required}")
    private String nameEn;

    @NotBlank(message = "{validation.appliance.nameUa.required}")
    private String nameUa;

    private String descriptionEn;
    private String descriptionUa;

    @NotNull(message = "{validation.appliance.price.required}")
    @Min(value = 0, message = "{validation.appliance.price.min}")
    private BigDecimal price;

    @NotNull(message = "{validation.appliance.category.required}")
    private Long categoryId;

    @NotNull(message = "{validation.appliance.manufacturer.required}")
    private Long manufacturerId;

    @NotNull(message = "{validation.appliance.stock.required}")
    @Min(value = 0, message = "{validation.appliance.stock.min}")
    private Integer stockQuantity;

    private String model;
    private Integer power;
    private String powerType;
}