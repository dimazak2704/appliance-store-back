package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.model.Manufacturer;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;
import com.epam.dimazak.appliances.repository.ManufacturerRepository;
import com.epam.dimazak.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @Override
    @Loggable
    public List<ManufacturerDto> getAllManufacturers() {
        return manufacturerRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Loggable
    public ManufacturerDto getManufacturerById(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.manufacturer.not_found")));
        return mapToDto(manufacturer);
    }

    @Override
    @Transactional
    @Loggable
    public ManufacturerDto createManufacturer(ManufacturerRequestDto request) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(request.getName());
        return mapToDto(manufacturerRepository.save(manufacturer));
    }

    @Override
    @Transactional
    @Loggable
    public ManufacturerDto updateManufacturer(Long id, ManufacturerRequestDto request) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.manufacturer.not_found")));

        manufacturer.setName(request.getName());
        return mapToDto(manufacturerRepository.save(manufacturer));
    }

    @Override
    @Transactional
    @Loggable
    public void deleteManufacturer(Long id) {
        if (!manufacturerRepository.existsById(id)) {
            throw new ResourceNotFoundException(getMsg("error.manufacturer.not_found"));
        }
        try {
            manufacturerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessRuleException(getMsg("error.manufacturer.delete_constraint"));
        }
    }

    private ManufacturerDto mapToDto(Manufacturer manufacturer) {
        return ManufacturerDto.builder()
                .id(manufacturer.getId())
                .name(manufacturer.getName())
                .build();
    }
}