package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Appliance;
import com.epam.dimazak.appliances.model.Category;
import com.epam.dimazak.appliances.model.Manufacturer;
import com.epam.dimazak.appliances.model.PowerType;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import com.epam.dimazak.appliances.repository.ApplianceRepository;
import com.epam.dimazak.appliances.repository.CategoryRepository;
import com.epam.dimazak.appliances.repository.ManufacturerRepository;
import com.epam.dimazak.appliances.repository.specification.ApplianceSpecification;
import com.epam.dimazak.appliances.service.ApplianceService;
import com.epam.dimazak.appliances.service.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final FileStorageService fileStorageService;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final MessageSource messageSource;

    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Loggable
    public ApplianceDto getApplianceById(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.appliance.not_found") + " id: " + id));
        return mapToDto(appliance);
    }

    @Override
    @Transactional
    @Loggable
    public ApplianceDto uploadImage(Long id, MultipartFile file) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.appliance.not_found") + " id: " + id));

        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "/images/" + fileName;

        appliance.setImageUrl(fileUrl);
        applianceRepository.save(appliance);

        return mapToDto(appliance);
    }

    @Override
    @Loggable
    public Page<ApplianceDto> getAppliancesByFilter(ApplianceFilterDto filter, Pageable pageable) {
        return applianceRepository.findAll(ApplianceSpecification.getSpecifications(filter), pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public ApplianceDto createAppliance(ApplianceRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.category.not_found")));

        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.manufacturer.not_found")));

        Appliance appliance = new Appliance();
        mapRequestToEntity(request, appliance);

        appliance.setCategory(category);
        appliance.setManufacturer(manufacturer);
        appliance.setActive(true);

        applianceRepository.save(appliance);
        return mapToDto(appliance);
    }

    @Override
    @Transactional
    public ApplianceDto updateAppliance(Long id, ApplianceRequestDto request) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appliance not found"));

        if (!appliance.getCategory().getId().equals(request.getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.category.not_found")));
            appliance.setCategory(category);
        }

        if (!appliance.getManufacturer().getId().equals(request.getManufacturerId())) {
            Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                    .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.manufacturer.not_found")));
            appliance.setManufacturer(manufacturer);
        }

        mapRequestToEntity(request, appliance);
        applianceRepository.save(appliance);

        return mapToDto(appliance);
    }

    @Override
    @Transactional
    public void deleteAppliance(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appliance not found"));

        appliance.setActive(false);
        applianceRepository.save(appliance);
    }

    private void mapRequestToEntity(ApplianceRequestDto request, Appliance appliance) {
        appliance.setNameEn(request.getNameEn());
        appliance.setNameUa(request.getNameUa());
        appliance.setDescriptionEn(request.getDescriptionEn());
        appliance.setDescriptionUa(request.getDescriptionUa());
        appliance.setPrice(request.getPrice());
        appliance.setStockQuantity(request.getStockQuantity());
        appliance.setModel(request.getModel());
        appliance.setPower(request.getPower());

        if (request.getPowerType() != null && !request.getPowerType().isEmpty()) {
            try {
                appliance.setPowerType(PowerType.valueOf(request.getPowerType()));
            } catch (IllegalArgumentException e) {
                throw new BusinessRuleException(getMsg("error.appliance.invalid_power_type", request.getPowerType()));
            }
        }
    }

    private ApplianceDto mapToDto(Appliance appliance) {
        return ApplianceDto.builder()
                .id(appliance.getId())
                .nameEn(appliance.getNameEn())
                .nameUa(appliance.getNameUa())
                .descriptionEn(appliance.getDescriptionEn())
                .descriptionUa(appliance.getDescriptionUa())
                .price(appliance.getPrice())
                .imageUrl(appliance.getImageUrl())
                .active(appliance.isActive())
                .stockQuantity(appliance.getStockQuantity())
                .categoryId(appliance.getCategory().getId())
                .categoryNameEn(appliance.getCategory().getNameEn())
                .categoryNameUa(appliance.getCategory().getNameUa())
                .manufacturerId(appliance.getManufacturer().getId())
                .manufacturerName(appliance.getManufacturer().getName())
                .createdAt(appliance.getCreatedAt())
                .model(appliance.getModel())
                .power(appliance.getPower())
                .powerType(appliance.getPowerType() != null ? appliance.getPowerType().name() : null)
                .build();
    }
}