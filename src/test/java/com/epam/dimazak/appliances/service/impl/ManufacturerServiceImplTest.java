package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Manufacturer;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;
import com.epam.dimazak.appliances.repository.ManufacturerRepository;
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
class ManufacturerServiceImplTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    @Test
    void getAllManufacturers_returnsListOfManufacturers() {
        Manufacturer man1 = new Manufacturer(1L, "Samsung");
        Manufacturer man2 = new Manufacturer(2L, "LG");

        when(manufacturerRepository.findAll()).thenReturn(Arrays.asList(man1, man2));

        List<ManufacturerDto> result = manufacturerService.getAllManufacturers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Samsung");
    }

    @Test
    void getManufacturerById_whenExists_returnsDto() {
        Long id = 1L;
        Manufacturer manufacturer = new Manufacturer(id, "Samsung");

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(manufacturer));

        ManufacturerDto result = manufacturerService.getManufacturerById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Samsung");
    }

    @Test
    void getManufacturerById_whenNotExists_throwsException() {
        Long id = 99L;

        when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Not found");

        assertThatThrownBy(() -> manufacturerService.getManufacturerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Not found");
    }

    @Test
    void createManufacturer_savesAndReturnsDto() {
        ManufacturerRequestDto request = new ManufacturerRequestDto();
        request.setName("Bosch");

        Manufacturer saved = new Manufacturer(1L, "Bosch");

        when(manufacturerRepository.save(any(Manufacturer.class))).thenReturn(saved);

        ManufacturerDto result = manufacturerService.createManufacturer(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Bosch");
        verify(manufacturerRepository).save(any(Manufacturer.class));
    }

    @Test
    void updateManufacturer_whenExists_updatesAndReturnsDto() {
        Long id = 1L;
        ManufacturerRequestDto request = new ManufacturerRequestDto();
        request.setName("Updated");

        Manufacturer existing = new Manufacturer(id, "Old");

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(manufacturerRepository.save(existing)).thenReturn(existing);

        ManufacturerDto result = manufacturerService.updateManufacturer(id, request);

        assertThat(existing.getName()).isEqualTo("Updated");
        assertThat(result.getName()).isEqualTo("Updated");
    }

    @Test
    void deleteManufacturer_whenExists_deletesSuccessfully() {
        Long id = 1L;

        when(manufacturerRepository.existsById(id)).thenReturn(true);

        manufacturerService.deleteManufacturer(id);

        verify(manufacturerRepository).deleteById(id);
    }

    @Test
    void deleteManufacturer_whenConstraintViolation_throwsBusinessRuleException() {
        Long id = 1L;

        when(manufacturerRepository.existsById(id)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("Constraint")).when(manufacturerRepository).deleteById(id);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cannot delete");

        assertThatThrownBy(() -> manufacturerService.deleteManufacturer(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Cannot delete");
    }
}
