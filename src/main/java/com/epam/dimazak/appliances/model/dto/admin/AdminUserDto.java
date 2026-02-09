package com.epam.dimazak.appliances.model.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String card;
    private boolean isEnabled;
}