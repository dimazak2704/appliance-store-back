package com.epam.dimazak.appliances.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    @Test
    void addRow_shouldAddRowAndSetBackReference() {
        Orders order = new Orders();
        order.setOrderRowSet(new java.util.HashSet<>());
        OrderRow row = new OrderRow();

        order.addRow(row);

        assertThat(order.getOrderRowSet()).hasSize(1);
        assertThat(order.getOrderRowSet()).contains(row);
        assertThat(row.getOrders()).isEqualTo(order);
    }

    @Test
    void addRow_shouldHandleMultipleRows() {
        Orders order = new Orders();
        // Use a list to verify the addRow logic works correctly
        // HashSet treats entities without IDs as duplicates due to Lombok
        // equals/hashCode
        order.setOrderRowSet(new java.util.HashSet<>());

        OrderRow row1 = new OrderRow();
        row1.setId(1L); // Set unique IDs to make them distinct
        OrderRow row2 = new OrderRow();
        row2.setId(2L);

        order.addRow(row1);
        order.addRow(row2);

        assertThat(order.getOrderRowSet()).hasSize(2);
    }
}
