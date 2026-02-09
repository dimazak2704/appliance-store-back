package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;
import com.epam.dimazak.appliances.repository.CategoryRepository;
import com.epam.dimazak.appliances.repository.ManufacturerRepository;
import com.epam.dimazak.appliances.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    @Loggable
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll(Sort.by("id"))
                .stream()
                .map(c -> CategoryDto.builder()
                        .id(c.getId())
                        .nameEn(c.getNameEn())
                        .nameUa(c.getNameUa())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Loggable
    public List<ManufacturerDto> getAllManufacturers() {
        return manufacturerRepository.findAll(Sort.by("name"))
                .stream()
                .map(m -> ManufacturerDto.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .build())
                .collect(Collectors.toList());
    }
}