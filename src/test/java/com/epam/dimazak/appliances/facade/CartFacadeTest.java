package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.cart.CartDto;
import com.epam.dimazak.appliances.model.dto.cart.CheckoutRequestDto;
import com.epam.dimazak.appliances.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartFacadeTest {

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartFacade cartFacade;

    private static final String EMAIL = "test@example.com";

    @Test
    void addToCart_shouldCallService() {
        Long applianceId = 1L;
        when(authentication.getName()).thenReturn(EMAIL);

        cartFacade.addToCart(applianceId, authentication);

        verify(cartService).addToCart(EMAIL, applianceId);
    }

    @Test
    void getMyCart_shouldReturnCartDto() {
        CartDto expectedCart = new CartDto();
        when(authentication.getName()).thenReturn(EMAIL);
        when(cartService.getMyCart(EMAIL)).thenReturn(expectedCart);

        CartDto result = cartFacade.getMyCart(authentication);

        assertThat(result).isEqualTo(expectedCart);
        verify(cartService).getMyCart(EMAIL);
    }

    @Test
    void removeItem_shouldCallService() {
        Long applianceId = 1L;
        when(authentication.getName()).thenReturn(EMAIL);

        cartFacade.removeItem(applianceId, authentication);

        verify(cartService).removeItem(EMAIL, applianceId);
    }

    @Test
    void updateQuantity_shouldCallService() {
        Long applianceId = 1L;
        Integer quantity = 5;
        when(authentication.getName()).thenReturn(EMAIL);

        cartFacade.updateQuantity(applianceId, quantity, authentication);

        verify(cartService).updateQuantity(EMAIL, applianceId, quantity);
    }

    @Test
    void checkout_shouldReturnOrderId() {
        CheckoutRequestDto request = new CheckoutRequestDto();
        Long expectedOrderId = 100L;
        when(authentication.getName()).thenReturn(EMAIL);
        when(cartService.checkout(EMAIL, request)).thenReturn(expectedOrderId);

        Long result = cartFacade.checkout(request, authentication);

        assertThat(result).isEqualTo(expectedOrderId);
        verify(cartService).checkout(EMAIL, request);
    }
}
