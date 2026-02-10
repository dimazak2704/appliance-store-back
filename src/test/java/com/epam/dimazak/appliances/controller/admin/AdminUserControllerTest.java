package com.epam.dimazak.appliances.controller.admin;

import com.epam.dimazak.appliances.facade.admin.AdminUserFacade;
import com.epam.dimazak.appliances.model.dto.admin.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private AdminUserFacade adminUserFacade;

    private AdminUserController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminUserController(adminUserFacade);
    }

    @Test
    void getAll_shouldReturnOkAndPageOfUsers() {
        AdminUserDto userDto = AdminUserDto.builder().id(1L).build();
        Page<AdminUserDto> page = new PageImpl<>(List.of(userDto));

        when(adminUserFacade.getAllUsers(any(UserFilterDto.class), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<AdminUserDto>> response = controller.getAll(null, null, null, Pageable.unpaged());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(adminUserFacade).getAllUsers(any(UserFilterDto.class), any(Pageable.class));
    }

    @Test
    void getById_shouldReturnOkAndUserDto() {
        Long id = 1L;
        AdminUserDto userDto = AdminUserDto.builder().id(id).build();

        when(adminUserFacade.getUserById(id)).thenReturn(userDto);

        ResponseEntity<AdminUserDto> response = controller.getById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
        verify(adminUserFacade).getUserById(id);
    }

    @Test
    void create_shouldReturnOkAndUserDto() {
        UserCreateRequest request = new UserCreateRequest();
        AdminUserDto userDto = AdminUserDto.builder().id(1L).build();

        when(adminUserFacade.createUser(any(UserCreateRequest.class))).thenReturn(userDto);

        ResponseEntity<AdminUserDto> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
        verify(adminUserFacade).createUser(request);
    }

    @Test
    void update_shouldReturnOkAndUserDto() {
        Long id = 1L;
        UserUpdateRequest request = new UserUpdateRequest();
        AdminUserDto userDto = AdminUserDto.builder().id(id).build();

        when(adminUserFacade.updateUser(eq(id), any(UserUpdateRequest.class))).thenReturn(userDto);

        ResponseEntity<AdminUserDto> response = controller.update(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
        verify(adminUserFacade).updateUser(id, request);
    }

    @Test
    void updateRole_shouldReturnOk() {
        Long id = 1L;
        UserRoleRequest request = new UserRoleRequest();

        ResponseEntity<Void> response = controller.updateRole(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(adminUserFacade).updateUserRole(id, request);
    }

    @Test
    void updateStatus_shouldReturnOk() {
        Long id = 1L;
        boolean enabled = true;

        ResponseEntity<Void> response = controller.updateStatus(id, enabled);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(adminUserFacade).updateUserStatus(id, enabled);
    }

    @Test
    void delete_shouldReturnOk() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.delete(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(adminUserFacade).deleteUser(id);
    }
}
