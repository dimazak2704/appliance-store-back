package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Category;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;
import com.epam.dimazak.appliances.repository.CategoryRepository;
import com.epam.dimazak.appliances.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @Override
    @Loggable
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Loggable
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.category.not_found")));
        return mapToDto(category);
    }

    @Override
    @Transactional
    @Loggable
    public CategoryDto createCategory(CategoryRequestDto request) {
        Category category = new Category();
        category.setNameEn(request.getNameEn());
        category.setNameUa(request.getNameUa());
        return mapToDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    @Loggable
    public CategoryDto updateCategory(Long id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.category.not_found")));

        category.setNameEn(request.getNameEn());
        category.setNameUa(request.getNameUa());
        return mapToDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    @Loggable
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(getMsg("error.category.not_found"));
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessRuleException(getMsg("error.category.delete_constraint"));
        }
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .nameEn(category.getNameEn())
                .nameUa(category.getNameUa())
                .build();
    }
}