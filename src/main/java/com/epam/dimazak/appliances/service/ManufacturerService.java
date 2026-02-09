package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;

import java.util.List;

public interface ManufacturerService {
    List<ManufacturerDto> getAllManufacturers();
    ManufacturerDto getManufacturerById(Long id);
    ManufacturerDto createManufacturer(ManufacturerRequestDto request);
    ManufacturerDto updateManufacturer(Long id, ManufacturerRequestDto request);
    void deleteManufacturer(Long id);
}