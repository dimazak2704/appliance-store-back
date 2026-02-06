package com.epam.dimazak.appliances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appliance")
public class Appliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String model;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(name = "power_type")
    private PowerType powerType;

    private String characteristic;

    @Column(length = 1000)
    private String description;

    @Positive
    private Integer power;

    @NotNull
    @Positive
    private BigDecimal price;
}