package com.epam.dimazak.appliances.exception;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final MessageSource messageSource = mock(MessageSource.class);
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(messageSource);

    @Test
    void handleNotEnoughStock_shouldReturnConflict() {
        NotEnoughStockException ex = new NotEnoughStockException("Stock error");
        ResponseEntity<Object> response = globalExceptionHandler.handleNotEnoughStock(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Stock error");
    }

    @Test
    void handleOrderLogic_shouldReturnBadRequest() {
        OrderLogicException ex = new OrderLogicException("Order logic error");
        ResponseEntity<Object> response = globalExceptionHandler.handleOrderLogic(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Order logic error");
    }

    @Test
    void handleAccessDenied_shouldReturnForbidden() {
        UserAccessDeniedException ex = new UserAccessDeniedException("Access denied");
        ResponseEntity<Object> response = globalExceptionHandler.handleAccessDenied(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Access denied");
    }

    @Test
    void handleBusinessRule_shouldReturnBadRequest() {
        BusinessRuleException ex = new BusinessRuleException("Business rule violation");
        ResponseEntity<Object> response = globalExceptionHandler.handleBusinessRule(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Business rule violation");
    }

    @Test
    void handleCartEmpty_shouldReturnBadRequest() {
        CartEmptyException ex = new CartEmptyException("Cart empty");
        ResponseEntity<Object> response = globalExceptionHandler.handleCartEmpty(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Cart empty");
    }

    @Test
    void handleResourceNotFound_shouldReturnNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<Object> response = globalExceptionHandler.handleResourceNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Not found");
    }

    @Test
    void handleFileStorageException_shouldReturnInternalServerError() {
        FileStorageException ex = new FileStorageException("File storage error");
        ResponseEntity<Object> response = globalExceptionHandler.handleFileStorageException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("File storage error");
    }

    @Test
    void handleInvalidToken_shouldReturnBadRequest() {
        InvalidTokenException ex = new InvalidTokenException("Invalid token");
        ResponseEntity<Object> response = globalExceptionHandler.handleInvalidToken(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Invalid token");
    }

    @Test
    void handleUserExists_shouldReturnConflict() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User exists");
        ResponseEntity<Object> response = globalExceptionHandler.handleUserExists(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("User exists");
    }

    @Test
    void handleEmailError_shouldReturnInternalServerError() {
        EmailSendingException ex = new EmailSendingException("Email error");
        when(messageSource.getMessage(eq("error.email.sending"), any(), any()))
                .thenReturn("Failed to send email notification");

        ResponseEntity<Object> response = globalExceptionHandler.handleEmailError(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Failed to send email notification");
    }

    @Test
    void handleDisabledUser_shouldReturnForbidden() {
        DisabledException ex = new DisabledException("Disabled");
        when(messageSource.getMessage(eq("error.user.disabled"), any(), any()))
                .thenReturn("Account is disabled. Please check your email.");

        ResponseEntity<Object> response = globalExceptionHandler.handleDisabledUser(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Account is disabled. Please check your email.");
    }

    @Test
    void handleBadCredentials_shouldReturnUnauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        when(messageSource.getMessage(eq("error.auth.bad_credentials"), any(), any()))
                .thenReturn("Invalid email or password");

        ResponseEntity<Object> response = globalExceptionHandler.handleBadCredentials(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Invalid email or password");
    }

    @Test
    void handleGeneralException_shouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");
        when(messageSource.getMessage(eq("error.unexpected"), any(), any())).thenReturn("An unexpected error occurred");

        ResponseEntity<Object> response = globalExceptionHandler.handleGeneralException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("An unexpected error occurred");
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithDetails() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "errorMessage");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(messageSource.getMessage(eq("error.validation.failed"), any(), any())).thenReturn("Validation Failed");

        ResponseEntity<Object> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Validation Failed");
        assertThat(body.get("details")).isInstanceOf(Map.class);
        Map<?, ?> details = (Map<?, ?>) body.get("details");
        assertThat(details.get("fieldName")).isEqualTo("errorMessage");
    }
}
