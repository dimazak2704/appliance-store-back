package com.epam.dimazak.appliances.controller.admin;

import com.epam.dimazak.appliances.facade.admin.AdminApplianceFacade;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/appliances")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
public class AdminApplianceController {

    private final AdminApplianceFacade adminApplianceFacade;

    @PostMapping
    public ResponseEntity<ApplianceDto> create(@RequestBody @Valid ApplianceRequestDto request) {
        return ResponseEntity.ok(adminApplianceFacade.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplianceDto> update(
            @PathVariable Long id,
            @RequestBody @Valid ApplianceRequestDto request
    ) {
        return ResponseEntity.ok(adminApplianceFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminApplianceFacade.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplianceDto> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(adminApplianceFacade.uploadImage(id, file));
    }
}