package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.CategoryFacade;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryFacade categoryFacade;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        return ResponseEntity.ok(categoryFacade.getAll());
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryRequestDto request) {
        return ResponseEntity.ok(categoryFacade.create(request));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto request) {
        return ResponseEntity.ok(categoryFacade.update(id, request));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryFacade.delete(id);
        return ResponseEntity.ok().build();
    }
}