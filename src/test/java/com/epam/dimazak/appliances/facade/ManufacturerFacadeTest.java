package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;
import com.epam.dimazak.appliances.service.ManufacturerService;
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
class ManufacturerFacadeTest {

    @Mock
    private ManufacturerService manufacturerService;

    @InjectMocks
    private ManufacturerFacade manufacturerFacade;

    @Test
    void getAll_shouldReturnListOfManufacturers() {
        List<ManufacturerDto> expectedList = Collections.emptyList();
        when(manufacturerService.getAllManufacturers()).thenReturn(expectedList);

        List<ManufacturerDto> result = manufacturerFacade.getAll();

        assertThat(result).isEqualTo(expectedList);
        verify(manufacturerService).getAllManufacturers();
    }

    @Test
    void create_shouldReturnCreatedManufacturer() {
        ManufacturerRequestDto request = new ManufacturerRequestDto();
        ManufacturerDto expectedDto = new ManufacturerDto();
        when(manufacturerService.createManufacturer(request)).thenReturn(expectedDto);

        ManufacturerDto result = manufacturerFacade.create(request);

        assertThat(result).isEqualTo(expectedDto);
        verify(manufacturerService).createManufacturer(request);
    }

    @Test
    void update_shouldReturnUpdatedManufacturer() {
        Long id = 1L;
        ManufacturerRequestDto request = new ManufacturerRequestDto();
        ManufacturerDto expectedDto = new ManufacturerDto();
        when(manufacturerService.updateManufacturer(id, request)).thenReturn(expectedDto);

        ManufacturerDto result = manufacturerFacade.update(id, request);

        assertThat(result).isEqualTo(expectedDto);
        verify(manufacturerService).updateManufacturer(id, request);
    }

    @Test
    void delete_shouldCallService() {
        Long id = 1L;
        manufacturerFacade.delete(id);
        verify(manufacturerService).deleteManufacturer(id);
    }
}
