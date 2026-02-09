package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.dictionary.CategoryDto;
import com.epam.dimazak.appliances.model.dto.dictionary.ManufacturerDto;

import java.util.List;

public interface DictionaryService {
    List<CategoryDto> getAllCategories();
    List<ManufacturerDto> getAllManufacturers();
}