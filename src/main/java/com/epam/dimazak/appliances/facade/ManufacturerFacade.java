package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerRequestDto;
import com.epam.dimazak.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ManufacturerFacade {
    private final ManufacturerService manufacturerService;

    public List<ManufacturerDto> getAll() {
        return manufacturerService.getAllManufacturers();
    }

    public ManufacturerDto create(ManufacturerRequestDto request) {
        return manufacturerService.createManufacturer(request);
    }

    public ManufacturerDto update(Long id, ManufacturerRequestDto request) {
        return manufacturerService.updateManufacturer(id, request);
    }

    public void delete(Long id) {
        manufacturerService.deleteManufacturer(id);
    }
}