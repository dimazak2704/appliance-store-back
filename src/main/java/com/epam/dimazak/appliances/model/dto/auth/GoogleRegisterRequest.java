package com.epam.dimazak.appliances.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GoogleRegisterRequest {

    @NotBlank(message = "{validation.google.token}")
    private String token;

    @NotBlank(message = "{validation.password.required}")
    @Size(min = 8, message = "{validation.password.min}")
    private String password;

    @Size(min = 16, max = 16, message = "{validation.card.size}")
    private String card;
}