package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.ManufacturerFacade;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;
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
class ManufacturerControllerTest {

    @Mock
    private ManufacturerFacade manufacturerFacade;

    private ManufacturerController controller;

    @BeforeEach
    void setUp() {
        controller = new ManufacturerController(manufacturerFacade);
    }

    @Test
    void getAll_shouldReturnOkAndListOfManufacturers() {
        ManufacturerDto manufacturerDto = ManufacturerDto.builder().id(1L).build();
        List<ManufacturerDto> manufacturers = List.of(manufacturerDto);

        when(manufacturerFacade.getAll()).thenReturn(manufacturers);

        ResponseEntity<List<ManufacturerDto>> response = controller.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(manufacturers);
        verify(manufacturerFacade).getAll();
    }

    @Test
    void create_shouldReturnOkAndManufacturerDto() {
        ManufacturerRequestDto request = new ManufacturerRequestDto();
        ManufacturerDto manufacturerDto = ManufacturerDto.builder().id(1L).build();

        when(manufacturerFacade.create(any(ManufacturerRequestDto.class))).thenReturn(manufacturerDto);

        ResponseEntity<ManufacturerDto> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(manufacturerDto);
        verify(manufacturerFacade).create(request);
    }

    @Test
    void update_shouldReturnOkAndManufacturerDto() {
        Long id = 1L;
        ManufacturerRequestDto request = new ManufacturerRequestDto();
        ManufacturerDto manufacturerDto = ManufacturerDto.builder().id(id).build();

        when(manufacturerFacade.update(eq(id), any(ManufacturerRequestDto.class))).thenReturn(manufacturerDto);

        ResponseEntity<ManufacturerDto> response = controller.update(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(manufacturerDto);
        verify(manufacturerFacade).update(id, request);
    }

    @Test
    void delete_shouldReturnOk() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.delete(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(manufacturerFacade).delete(id);
    }
}
