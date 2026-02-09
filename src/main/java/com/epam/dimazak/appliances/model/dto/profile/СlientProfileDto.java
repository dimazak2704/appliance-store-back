package com.epam.dimazak.appliances.model.dto.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class СlientProfileDto {
    private String name;
    private String email;
    private String card;
    private String role;
}