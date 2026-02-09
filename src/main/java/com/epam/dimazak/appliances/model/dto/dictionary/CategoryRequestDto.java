package com.epam.dimazak.appliances.model.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "{validation.category.nameEn.required}")
    private String nameEn;

    @NotBlank(message = "{validation.category.nameUa.required}")
    private String nameUa;
}