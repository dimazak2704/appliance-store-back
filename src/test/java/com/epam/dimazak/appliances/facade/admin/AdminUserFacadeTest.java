package com.epam.dimazak.appliances.facade.admin;

import com.epam.dimazak.appliances.model.dto.admin.*;
import com.epam.dimazak.appliances.service.ClientService;
import com.epam.dimazak.appliances.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserFacadeTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private AdminUserFacade adminUserFacade;

    @Test
    void getAllUsers_shouldReturnPageOfUsers() {
        UserFilterDto filter = new UserFilterDto();
        Pageable pageable = Pageable.unpaged();
        Page<AdminUserDto> expectedPage = new PageImpl<>(Collections.emptyList());
        when(clientService.getAllUsers(filter, pageable)).thenReturn(expectedPage);

        Page<AdminUserDto> result = adminUserFacade.getAllUsers(filter, pageable);

        assertThat(result).isEqualTo(expectedPage);
        verify(clientService).getAllUsers(filter, pageable);
    }

    @Test
    void getUserById_shouldReturnUser() {
        Long id = 1L;
        AdminUserDto expectedDto = new AdminUserDto();
        when(clientService.getUserById(id)).thenReturn(expectedDto);

        AdminUserDto result = adminUserFacade.getUserById(id);

        assertThat(result).isEqualTo(expectedDto);
        verify(clientService).getUserById(id);
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        UserCreateRequest request = new UserCreateRequest();
        AdminUserDto expectedDto = new AdminUserDto();
        when(clientService.createUser(request)).thenReturn(expectedDto);

        AdminUserDto result = adminUserFacade.createUser(request);

        assertThat(result).isEqualTo(expectedDto);
        verify(clientService).createUser(request);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        Long id = 1L;
        UserUpdateRequest request = new UserUpdateRequest();
        AdminUserDto expectedDto = new AdminUserDto();
        when(clientService.updateUser(id, request)).thenReturn(expectedDto);

        AdminUserDto result = adminUserFacade.updateUser(id, request);

        assertThat(result).isEqualTo(expectedDto);
        verify(clientService).updateUser(id, request);
    }

    @Test
    void updateUserRole_shouldCallService() {
        Long id = 1L;
        UserRoleRequest request = new UserRoleRequest();
        request.setRole(Role.ADMIN.name());

        adminUserFacade.updateUserRole(id, request);

        verify(clientService).updateUserRole(id, Role.ADMIN.name());
    }

    @Test
    void updateUserStatus_shouldCallService() {
        Long id = 1L;
        boolean enabled = true;

        adminUserFacade.updateUserStatus(id, enabled);

        verify(clientService).updateUserStatus(id, enabled);
    }

    @Test
    void deleteUser_shouldCallService() {
        Long id = 1L;
        adminUserFacade.deleteUser(id);
        verify(clientService).deleteUser(id);
    }
}
