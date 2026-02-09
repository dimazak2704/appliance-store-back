package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.ManufacturerFacade;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerFacade manufacturerFacade;

    @GetMapping
    public ResponseEntity<List<ManufacturerDto>> getAll() {
        return ResponseEntity.ok(manufacturerFacade.getAll());
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PostMapping
    public ResponseEntity<ManufacturerDto> create(@RequestBody @Valid ManufacturerRequestDto request) {
        return ResponseEntity.ok(manufacturerFacade.create(request));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDto> update(@PathVariable Long id, @RequestBody @Valid ManufacturerRequestDto request) {
        return ResponseEntity.ok(manufacturerFacade.update(id, request));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        manufacturerFacade.delete(id);
        return ResponseEntity.ok().build();
    }
}