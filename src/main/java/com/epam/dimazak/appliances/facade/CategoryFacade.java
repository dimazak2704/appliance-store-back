package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;
import com.epam.dimazak.appliances.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryFacade {
    private final CategoryService categoryService;

    public List<CategoryDto> getAll() {
        return categoryService.getAllCategories();
    }

    public CategoryDto create(CategoryRequestDto request) {
        return categoryService.createCategory(request);
    }

    public CategoryDto update(Long id, CategoryRequestDto request) {
        return categoryService.updateCategory(id, request);
    }

    public void delete(Long id) {
        categoryService.deleteCategory(id);
    }
}