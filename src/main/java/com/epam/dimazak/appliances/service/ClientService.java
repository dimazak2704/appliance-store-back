package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.admin.AdminUserDto;
import com.epam.dimazak.appliances.model.dto.admin.UserCreateRequest;
import com.epam.dimazak.appliances.model.dto.admin.UserFilterDto;
import com.epam.dimazak.appliances.model.dto.admin.UserUpdateRequest;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    СlientProfileDto getProfile(String email);
    СlientProfileDto updateProfile(String email, UpdateProfileRequest request);
    void changePassword(String email, ChangePasswordRequest request);

    Page<AdminUserDto> getAllUsers(UserFilterDto filter, Pageable pageable);
    AdminUserDto getUserById(Long id);
    AdminUserDto createUser(UserCreateRequest request);
    AdminUserDto updateUser(Long id, UserUpdateRequest request);
    void updateUserRole(Long id, String roleName);
    void updateUserStatus(Long id, boolean isEnabled);
    void deleteUser(Long id);
    void adminResetPassword(Long id, String newPassword);
}