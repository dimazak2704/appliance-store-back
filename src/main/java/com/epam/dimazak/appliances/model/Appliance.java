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
    @Column(name = "name_en")
    private String nameEn;

    @NotNull
    @Column(name = "name_ua")
    private String nameUa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "power_type")
    private PowerType powerType;

    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(name = "description_ua", columnDefinition = "TEXT")
    private String descriptionUa;

    @Positive
    private Integer power;

    @NotNull
    @Positive
    private BigDecimal price;

    private String imageUrl;

    private Integer stockQuantity;

    private boolean active = true;
}