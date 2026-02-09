package com.epam.dimazak.appliances.model.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = "{validation.name.required}")
    @Size(min = 2, max = 50, message = "{validation.name.size}")
    private String name;

    @Size(min = 16, max = 16, message = "{validation.card.size}")
    private String card;
}