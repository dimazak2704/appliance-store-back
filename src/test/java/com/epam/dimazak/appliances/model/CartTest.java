package com.epam.dimazak.appliances.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    void addItem_shouldAddItemToCartAndSetBackReference() {
        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setQuantity(2);

        cart.addItem(item);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0)).isEqualTo(item);
        assertThat(item.getCart()).isEqualTo(cart);
    }

    @Test
    void clear_shouldRemoveAllItems() {
        Cart cart = new Cart();
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();

        cart.addItem(item1);
        cart.addItem(item2);
        assertThat(cart.getItems()).hasSize(2);

        cart.clear();

        assertThat(cart.getItems()).isEmpty();
    }
}
