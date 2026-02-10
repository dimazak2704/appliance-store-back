package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.CartEmptyException;
import com.epam.dimazak.appliances.exception.NotEnoughStockException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.*;
import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;
import com.epam.dimazak.appliances.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private OrdersRepository orderRepository;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void addToCart_whenNewItem_addsItem() {
        String email = "test@example.com";
        Long applianceId = 1L;

        Client client = new Client();
        client.setEmail(email);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        Appliance appliance = new Appliance();
        appliance.setId(applianceId);
        appliance.setPrice(BigDecimal.TEN);
        appliance.setStockQuantity(10);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));
        when(applianceRepository.findById(applianceId)).thenReturn(Optional.of(appliance));

        cartService.addToCart(email, applianceId);

        assertThat(cart.getItems()).hasSize(1);
        verify(cartRepository).save(cart);
    }

    @Test
    void addToCart_whenNotEnoughStock_throwsException() {
        String email = "test@example.com";
        Long applianceId = 1L;

        Client client = new Client();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        Appliance appliance = new Appliance();
        appliance.setId(applianceId);
        appliance.setStockQuantity(0);
        appliance.setNameEn("Toaster");

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));
        when(applianceRepository.findById(applianceId)).thenReturn(Optional.of(appliance));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Not enough");

        assertThatThrownBy(() -> cartService.addToCart(email, applianceId))
                .isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    void getMyCart_returnsCartDto() {
        String email = "test@example.com";
        Client client = new Client();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));

        CartDto result = cartService.getMyCart(email);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void clearCart_removesAllItems() {
        String email = "test@example.com";
        Client client = new Client();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.getItems().add(new CartItem());

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));

        cartService.clearCart(email);

        assertThat(cart.getItems()).isEmpty();
        verify(cartRepository).save(cart);
    }

    @Test
    void checkout_whenCartEmpty_throwsException() {
        String email = "test@example.com";
        Client client = new Client();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        CheckoutRequestDto request = new CheckoutRequestDto();

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Empty cart");

        assertThatThrownBy(() -> cartService.checkout(email, request))
                .isInstanceOf(CartEmptyException.class);
    }

    @Test
    void removeItem_whenExists_removesItem() {
        String email = "test@example.com";
        Long applianceId = 1L;

        Client client = new Client();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        Appliance appliance = new Appliance();
        appliance.setId(applianceId);

        CartItem item = new CartItem();
        item.setAppliance(appliance);
        cart.getItems().add(item);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(cartRepository.findByClient(client)).thenReturn(Optional.of(cart));

        cartService.removeItem(email, applianceId);

        assertThat(cart.getItems()).isEmpty();
        verify(cartRepository).save(cart);
    }
}
