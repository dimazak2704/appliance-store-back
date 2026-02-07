package com.epam.dimazak.appliances.service;

import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ApplianceService {
    ApplianceDto getApplianceById(Long id);
    ApplianceDto uploadImage(Long id, MultipartFile file);
    Page<ApplianceDto> getAppliancesByFilter(ApplianceFilterDto filter, Pageable pageable);
}