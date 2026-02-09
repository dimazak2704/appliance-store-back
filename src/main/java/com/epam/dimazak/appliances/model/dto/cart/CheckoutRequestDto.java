package com.epam.dimazak.appliances.model.dto.cart;

import com.epam.dimazak.appliances.model.DeliveryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequestDto {

    @NotBlank(message = "{validation.phone.required}")
    private String phone;

    @NotNull(message = "Delivery type is required")
    private DeliveryType deliveryType;

    private String address;
}