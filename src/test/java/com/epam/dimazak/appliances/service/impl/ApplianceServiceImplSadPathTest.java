package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.*;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import com.epam.dimazak.appliances.repository.ApplianceRepository;
import com.epam.dimazak.appliances.repository.CategoryRepository;
import com.epam.dimazak.appliances.repository.ManufacturerRepository;
import com.epam.dimazak.appliances.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceImplSadPathTest {

    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ApplianceServiceImpl applianceService;

    // ===== SAD PATH TESTS =====

    @Test
    void createAppliance_whenCategoryNotFound_throwsException() {
        ApplianceRequestDto request = new ApplianceRequestDto();
        request.setNameEn("Test");
        request.setCategoryId(999L);
        request.setManufacturerId(1L);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Category not found");

        assertThatThrownBy(() -> applianceService.createAppliance(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void createAppliance_whenManufacturerNotFound_throwsException() {
        ApplianceRequestDto request = new ApplianceRequestDto();
        request.setNameEn("Test");
        request.setCategoryId(1L);
        request.setManufacturerId(999L);

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(manufacturerRepository.findById(999L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Manufacturer not found");

        assertThatThrownBy(() -> applianceService.createAppliance(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Manufacturer not found");
    }

    @Test
    void createAppliance_whenInvalidPowerType_throwsBusinessRuleException() {
        ApplianceRequestDto request = new ApplianceRequestDto();
        request.setNameEn("Test");
        request.setCategoryId(1L);
        request.setManufacturerId(1L);
        request.setPowerType("INVALID_TYPE");

        Category category = new Category();
        Manufacturer manufacturer = new Manufacturer();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Invalid power type");

        assertThatThrownBy(() -> applianceService.createAppliance(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Invalid power type");
    }

    @Test
    void updateAppliance_whenApplianceNotFound_throwsException() {
        Long id = 999L;
        ApplianceRequestDto request = new ApplianceRequestDto();

        when(applianceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applianceService.updateAppliance(id, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateAppliance_whenCategoryNotFound_throwsException() {
        Long id = 1L;
        ApplianceRequestDto request = new ApplianceRequestDto();
        request.setCategoryId(999L);

        // Setup existing appliance with valid category/manufacturer
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        Manufacturer existingManufacturer = new Manufacturer();
        existingManufacturer.setId(1L);

        Appliance existing = new Appliance();
        existing.setId(id);
        existing.setCategory(existingCategory);
        existing.setManufacturer(existingManufacturer);

        when(applianceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Category not found"); // Added this line

        assertThatThrownBy(() -> applianceService.updateAppliance(id, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found"); // Added this line
    }

    @Test
    void updateAppliance_whenManufacturerNotFound_throwsException() {
        Long id = 1L;
        ApplianceRequestDto request = new ApplianceRequestDto();
        request.setCategoryId(1L);
        request.setManufacturerId(999L);

        // Setup existing appliance
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        Manufacturer existingManufacturer = new Manufacturer();
        existingManufacturer.setId(1L);

        Appliance existing = new Appliance();
        existing.setId(id);
        existing.setCategory(existingCategory);
        existing.setManufacturer(existingManufacturer);

        Category category = new Category();
        category.setId(1L); // Set ID for category

        when(applianceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(manufacturerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applianceService.updateAppliance(id, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteAppliance_whenNotFound_throwsException() {
        Long id = 999L;

        when(applianceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applianceService.deleteAppliance(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void uploadImage_whenApplianceNotFound_throwsException() {
        Long id = 999L;
        MultipartFile file = mock(MultipartFile.class);

        when(applianceRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Appliance not found");

        assertThatThrownBy(() -> applianceService.uploadImage(id, file))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Appliance not found");
    }
}
