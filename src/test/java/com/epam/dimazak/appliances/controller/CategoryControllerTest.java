package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.CategoryFacade;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryFacade categoryFacade;

    private CategoryController controller;

    @BeforeEach
    void setUp() {
        controller = new CategoryController(categoryFacade);
    }

    @Test
    void getAll_shouldReturnOkAndListOfCategories() {
        CategoryDto categoryDto = CategoryDto.builder().id(1L).build();
        List<CategoryDto> categories = List.of(categoryDto);

        when(categoryFacade.getAll()).thenReturn(categories);

        ResponseEntity<List<CategoryDto>> response = controller.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(categories);
        verify(categoryFacade).getAll();
    }

    @Test
    void create_shouldReturnOkAndCategoryDto() {
        CategoryRequestDto request = new CategoryRequestDto();
        CategoryDto categoryDto = CategoryDto.builder().id(1L).build();

        when(categoryFacade.create(any(CategoryRequestDto.class))).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(categoryDto);
        verify(categoryFacade).create(request);
    }

    @Test
    void update_shouldReturnOkAndCategoryDto() {
        Long id = 1L;
        CategoryRequestDto request = new CategoryRequestDto();
        CategoryDto categoryDto = CategoryDto.builder().id(id).build();

        when(categoryFacade.update(eq(id), any(CategoryRequestDto.class))).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = controller.update(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(categoryDto);
        verify(categoryFacade).update(id, request);
    }

    @Test
    void delete_shouldReturnOk() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.delete(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(categoryFacade).delete(id);
    }
}
