package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.ApplianceFacade;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/appliances")
@RequiredArgsConstructor
public class ApplianceController {

    private final ApplianceFacade applianceFacade;

    @GetMapping
    public ResponseEntity<Page<ApplianceDto>> getAll(
            @ModelAttribute ApplianceFilterDto filter,
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(applianceFacade.getAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplianceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applianceFacade.getById(id));
    }
}