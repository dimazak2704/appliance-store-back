package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ApplianceService {
    ApplianceDto getApplianceById(Long id);
    ApplianceDto uploadImage(Long id, MultipartFile file);
    Page<ApplianceDto> getAppliancesByFilter(ApplianceFilterDto filter, Pageable pageable);
    ApplianceDto createAppliance(ApplianceRequestDto request);
    ApplianceDto updateAppliance(Long id, ApplianceRequestDto request);
    void deleteAppliance(Long id);
}