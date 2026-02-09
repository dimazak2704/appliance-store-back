package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Appliance;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import com.epam.dimazak.appliances.repository.ApplianceRepository;
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
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
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