package com.epam.dimazak.appliances.config.security;

import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            return client.get();
        }

        var employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            return employee.get();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}