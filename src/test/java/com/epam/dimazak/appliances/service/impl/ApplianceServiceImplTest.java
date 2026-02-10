package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Appliance;
import com.epam.dimazak.appliances.model.Category;
import com.epam.dimazak.appliances.model.Manufacturer;
import com.epam.dimazak.appliances.model.PowerType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceImplTest {

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

    @Test
    void getApplianceById_whenExists_returnsDto() {
        Long id = 1L;
        Category category = new Category();
        category.setId(10L);
        category.setNameEn("Home");

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(20L);
        manufacturer.setName("Samsung");

        Appliance appliance = new Appliance();
        appliance.setId(id);
        appliance.setNameEn("Fridge");
        appliance.setNameUa("Холодильник");
        appliance.setPrice(BigDecimal.TEN);
        appliance.setCategory(category);
        appliance.setManufacturer(manufacturer);

        when(applianceRepository.findById(id)).thenReturn(Optional.of(appliance));

        ApplianceDto result = applianceService.getApplianceById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNameEn()).isEqualTo("Fridge");
        assertThat(result.getCategoryNameEn()).isEqualTo("Home");
        assertThat(result.getManufacturerName()).isEqualTo("Samsung");
    }

    @Test
    void getApplianceById_whenNotExists_throwsException() {
        Long id = 99L;
        when(applianceRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Not found");

        assertThatThrownBy(() -> applianceService.getApplianceById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Not found");
    }

    @Test
    void createAppliance_whenValid_returnsDto() {
        ApplianceRequestDto request = new ApplianceRequestDto();
        request.setNameEn("Mixer");
        request.setNameUa("MinxerUA");
        request.setPrice(BigDecimal.valueOf(150));
        request.setCategoryId(10L);
        request.setManufacturerId(20L);
        request.setPowerType("AC220");
        request.setPower(500);

        Category category = new Category();
        category.setId(10L);
        category.setNameEn("Kitchen");

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(20L);
        manufacturer.setName("Bosch");

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(manufacturerRepository.findById(20L)).thenReturn(Optional.of(manufacturer));
        when(applianceRepository.save(any(Appliance.class))).thenAnswer(invocation -> {
            Appliance saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ApplianceDto result = applianceService.createAppliance(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNameEn()).isEqualTo("Mixer");
        assertThat(result.getPowerType()).isEqualTo("AC220");
        assertThat(result.getCategoryNameEn()).isEqualTo("Kitchen");

        verify(applianceRepository).save(any(Appliance.class));
    }

    @Test
    void deleteAppliance_whenExists_setsActiveFalse() {
        Long id = 1L;
        Appliance appliance = new Appliance();
        appliance.setId(id);
        appliance.setActive(true);

        when(applianceRepository.findById(id)).thenReturn(Optional.of(appliance));

        applianceService.deleteAppliance(id);

        assertThat(appliance.isActive()).isFalse();
        verify(applianceRepository).save(appliance);
    }

    @Test
    void uploadImage_whenApplianceExists_updatesUrl() {
        Long id = 1L;
        Appliance appliance = new Appliance();
        appliance.setId(id);

        Category category = new Category();
        category.setId(1L);
        category.setNameEn("Cat");
        category.setNameUa("CatUa");
        appliance.setCategory(category);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Man");
        appliance.setManufacturer(manufacturer);

        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);

        when(applianceRepository.findById(id)).thenReturn(Optional.of(appliance));
        when(fileStorageService.storeFile(file)).thenReturn("test.jpg");
        when(applianceRepository.save(any(Appliance.class))).thenReturn(appliance);

        ApplianceDto result = applianceService.uploadImage(id, file);

        assertThat(result.getImageUrl()).isEqualTo("/images/test.jpg");
        verify(applianceRepository).save(appliance);
    }
}
