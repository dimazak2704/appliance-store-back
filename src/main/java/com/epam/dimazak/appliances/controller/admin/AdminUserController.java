package com.epam.dimazak.appliances.controller.admin;

import com.epam.dimazak.appliances.facade.admin.AdminUserFacade;
import com.epam.dimazak.appliances.model.dto.admin.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserFacade adminUserFacade;

    @GetMapping
    public ResponseEntity<Page<AdminUserDto>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        UserFilterDto filter = new UserFilterDto();
        filter.setSearch(search);
        filter.setRole(role);
        filter.setIsEnabled(enabled);

        return ResponseEntity.ok(adminUserFacade.getAllUsers(filter, pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserFacade.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<AdminUserDto> create(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.ok(adminUserFacade.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDto> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(adminUserFacade.updateUser(id, request));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody UserRoleRequest request) {
        adminUserFacade.updateUserRole(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        adminUserFacade.updateUserStatus(id, enabled);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminUserFacade.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/password-reset")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestBody String newPassword) {
        adminUserFacade.adminResetPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
}