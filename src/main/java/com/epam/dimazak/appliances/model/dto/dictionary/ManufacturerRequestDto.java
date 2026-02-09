package com.epam.dimazak.appliances.model.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManufacturerRequestDto {
    @NotBlank(message = "{validation.manufacturer.name.required}")
    private String name;
}