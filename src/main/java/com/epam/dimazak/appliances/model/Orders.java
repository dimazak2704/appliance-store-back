package com.epam.dimazak.appliances.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrderRow> orderRowSet = new HashSet<>();

    private Boolean approved = false;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String contactPhone;

    private BigDecimal totalAmount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void addRow(OrderRow row) {
        orderRowSet.add(row);
        row.setOrders(this);
    }
}