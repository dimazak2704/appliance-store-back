package com.epam.dimazak.appliances.config.security;

import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.Employee;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_whenClientExists_shouldReturnClient() {
        String email = "client@example.com";
        Client client = new Client();
        client.setEmail(email);
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertThat(result).isEqualTo(client);
        verify(clientRepository).findByEmail(email);
        verify(employeeRepository, never()).findByEmail(anyString());
    }

    @Test
    void loadUserByUsername_whenClientNotExistsButEmployeeExists_shouldReturnEmployee() {
        String email = "employee@example.com";
        Employee employee = new Employee();
        employee.setEmail(email);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertThat(result).isEqualTo(employee);
        verify(clientRepository).findByEmail(email);
        verify(employeeRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_whenUserNotFound_shouldThrowException() {
        String email = "unknown@example.com";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with email: " + email);

        verify(clientRepository).findByEmail(email);
        verify(employeeRepository).findByEmail(email);
    }
}
