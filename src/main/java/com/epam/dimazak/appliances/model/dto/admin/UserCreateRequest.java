package com.epam.dimazak.appliances.model.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank(message = "{validation.name.required}")
    private String name;

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.password.required}")
    @Size(min = 8, message = "{validation.password.min}")
    private String password;

    @NotBlank(message = "{validation.role.required}")
    private String role;

    @Size(min = 16, max = 16, message = "{validation.card.size}")
    private String card;
}