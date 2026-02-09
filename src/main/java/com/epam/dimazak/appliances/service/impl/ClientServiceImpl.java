package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.UserProfileDto;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.service.ClientService;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @Override
    @Loggable
    public UserProfileDto getProfile(String email) {
        Client client = getClient(email);
        return mapToDto(client);
    }

    @Override
    @Transactional
    @Loggable
    public UserProfileDto updateProfile(String email, UpdateProfileRequest request) {
        Client client = getClient(email);
        client.setName(request.getName());
        client.setCard(request.getCard());
        clientRepository.save(client);
        return mapToDto(client);
    }

    @Override
    @Transactional
    @Loggable
    public void changePassword(String email, ChangePasswordRequest request) {
        Client client = getClient(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), client.getPassword())) {
            throw new BadCredentialsException(getMsg("error.password.invalid"));
        }

        client.setPassword(passwordEncoder.encode(request.getNewPassword()));
        clientRepository.save(client);
    }

    private Client getClient(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
    }

    private UserProfileDto mapToDto(Client client) {
        return UserProfileDto.builder()
                .name(client.getName())
                .email(client.getEmail())
                .card(client.getCard())
                .role(client.getRole().name())
                .build();
    }
}