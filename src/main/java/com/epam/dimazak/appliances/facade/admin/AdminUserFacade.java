package com.epam.dimazak.appliances.facade.admin;

import com.epam.dimazak.appliances.model.dto.admin.*;
import com.epam.dimazak.appliances.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserFacade {

    private final ClientService clientService;

    public Page<AdminUserDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        return clientService.getAllUsers(filter, pageable);
    }

    public AdminUserDto getUserById(Long id) {
        return clientService.getUserById(id);
    }

    public AdminUserDto createUser(UserCreateRequest request) {
        return clientService.createUser(request);
    }

    public AdminUserDto updateUser(Long id, UserUpdateRequest request) {
        return clientService.updateUser(id, request);
    }

    public void updateUserRole(Long id, UserRoleRequest request) {
        clientService.updateUserRole(id, request.getRole());
    }

    public void updateUserStatus(Long id, boolean enabled) {
        clientService.updateUserStatus(id, enabled);
    }

    public void deleteUser(Long id) {
        clientService.deleteUser(id);
    }

    public void adminResetPassword(Long id, String newPassword) {
        clientService.adminResetPassword(id, newPassword);
    }
}