package com.epam.dimazak.appliances.model.dto.admin;

import lombok.Data;

@Data
public class UserFilterDto {
    private String search;
    private String role;
    private Boolean isEnabled;
}