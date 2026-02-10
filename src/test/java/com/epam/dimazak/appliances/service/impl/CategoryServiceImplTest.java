package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Category;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;
import com.epam.dimazak.appliances.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getAllCategories_returnsListOfCategories() {
        Category cat1 = new Category(1L, "Electronics", "Електроніка");
        Category cat2 = new Category(2L, "Home", "Дім");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNameEn()).isEqualTo("Electronics");
    }

    @Test
    void getCategoryById_whenExists_returnsDto() {
        Long id = 1L;
        Category category = new Category(id, "Electronics", "Електроніка");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.getCategoryById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNameEn()).isEqualTo("Electronics");
    }

    @Test
    void getCategoryById_whenNotExists_throwsException() {
        Long id = 99L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Not found");

        assertThatThrownBy(() -> categoryService.getCategoryById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Not found");
    }

    @Test
    void createCategory_savesAndReturnsDto() {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setNameEn("Kitchen");
        request.setNameUa("Кухня");

        Category category = new Category(null, "Kitchen", "Кухня");
        Category saved = new Category(1L, "Kitchen", "Кухня");

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryDto result = categoryService.createCategory(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNameEn()).isEqualTo("Kitchen");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_whenExists_updatesAndReturnsDto() {
        Long id = 1L;
        CategoryRequestDto request = new CategoryRequestDto();
        request.setNameEn("Updated");
        request.setNameUa("Оновлено");

        Category existing = new Category(id, "Old", "Старе");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        CategoryDto result = categoryService.updateCategory(id, request);

        assertThat(existing.getNameEn()).isEqualTo("Updated");
        assertThat(result.getNameEn()).isEqualTo("Updated");
    }

    @Test
    void deleteCategory_whenExists_deletesSuccessfully() {
        Long id = 1L;

        when(categoryRepository.existsById(id)).thenReturn(true);

        categoryService.deleteCategory(id);

        verify(categoryRepository).deleteById(id);
    }

    @Test
    void deleteCategory_whenNotExists_throwsException() {
        Long id = 99L;

        when(categoryRepository.existsById(id)).thenReturn(false);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Not found");

        assertThatThrownBy(() -> categoryService.deleteCategory(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteCategory_whenConstraintViolation_throwsBusinessRuleException() {
        Long id = 1L;

        when(categoryRepository.existsById(id)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("Constraint")).when(categoryRepository).deleteById(id);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cannot delete");

        assertThatThrownBy(() -> categoryService.deleteCategory(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Cannot delete");
    }
}
