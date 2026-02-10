package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;
import com.epam.dimazak.appliances.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryFacadeTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryFacade categoryFacade;

    @Test
    void getAll_shouldReturnListOfCategories() {
        List<CategoryDto> expectedList = Collections.emptyList();
        when(categoryService.getAllCategories()).thenReturn(expectedList);

        List<CategoryDto> result = categoryFacade.getAll();

        assertThat(result).isEqualTo(expectedList);
        verify(categoryService).getAllCategories();
    }

    @Test
    void create_shouldReturnCreatedCategory() {
        CategoryRequestDto request = new CategoryRequestDto();
        CategoryDto expectedDto = new CategoryDto();
        when(categoryService.createCategory(request)).thenReturn(expectedDto);

        CategoryDto result = categoryFacade.create(request);

        assertThat(result).isEqualTo(expectedDto);
        verify(categoryService).createCategory(request);
    }

    @Test
    void update_shouldReturnUpdatedCategory() {
        Long id = 1L;
        CategoryRequestDto request = new CategoryRequestDto();
        CategoryDto expectedDto = new CategoryDto();
        when(categoryService.updateCategory(id, request)).thenReturn(expectedDto);

        CategoryDto result = categoryFacade.update(id, request);

        assertThat(result).isEqualTo(expectedDto);
        verify(categoryService).updateCategory(id, request);
    }

    @Test
    void delete_shouldCallService() {
        Long id = 1L;
        categoryFacade.delete(id);
        verify(categoryService).deleteCategory(id);
    }
}
