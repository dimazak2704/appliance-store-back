package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.CartFacade;
import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartFacade cartFacade;

    private CartController controller;

    @BeforeEach
    void setUp() {
        controller = new CartController(cartFacade);
    }

    @Test
    void addToCart_shouldReturnOk() {
        Long applianceId = 1L;
        Authentication auth = mock(Authentication.class);

        ResponseEntity<Void> response = controller.addToCart(applianceId, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cartFacade).addToCart(applianceId, auth);
    }

    @Test
    void getCart_shouldReturnOkAndCartDto() {
        Authentication auth = mock(Authentication.class);
        CartDto cartDto = CartDto.builder().totalQuantity(5).build();

        when(cartFacade.getMyCart(auth)).thenReturn(cartDto);

        ResponseEntity<CartDto> response = controller.getCart(auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cartDto);
        verify(cartFacade).getMyCart(auth);
    }

    @Test
    void checkout_shouldReturnOkAndOrderId() {
        Authentication auth = mock(Authentication.class);
        CheckoutRequestDto request = new CheckoutRequestDto();
        Long orderId = 123L;

        when(cartFacade.checkout(any(CheckoutRequestDto.class), eq(auth))).thenReturn(orderId);

        ResponseEntity<Long> response = controller.checkout(request, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orderId);
        verify(cartFacade).checkout(request, auth);
    }

    @Test
    void removeItem_shouldReturnOk() {
        Long applianceId = 1L;
        Authentication auth = mock(Authentication.class);

        ResponseEntity<Void> response = controller.removeItem(applianceId, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cartFacade).removeItem(applianceId, auth);
    }

    @Test
    void updateQuantity_shouldReturnOk() {
        Long applianceId = 1L;
        Integer quantity = 2;
        Authentication auth = mock(Authentication.class);

        ResponseEntity<Void> response = controller.updateQuantity(applianceId, quantity, auth);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cartFacade).updateQuantity(applianceId, quantity, auth);
    }
}
