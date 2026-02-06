package com.epam.dimazak.appliances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client extends User {

    private String card;

    public Client(Long id, String name, String email, String password, String card, boolean isEnabled) {
        super(id, name, email, password, Role.CLIENT, isEnabled);
        this.card = card;
    }
}