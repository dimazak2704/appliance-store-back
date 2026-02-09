package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto createCategory(CategoryRequestDto request);
    CategoryDto updateCategory(Long id, CategoryRequestDto request);
    void deleteCategory(Long id);
}