package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import com.epam.dimazak.appliances.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ApplianceFacade {

    private final ApplianceService applianceService;

    public ApplianceDto getById(Long id) {
        return applianceService.getApplianceById(id);
    }

    public ApplianceDto uploadImage(Long id, MultipartFile file) {
        return applianceService.uploadImage(id, file);
    }

    public Page<ApplianceDto> getAll(ApplianceFilterDto filter, Pageable pageable) {
        return applianceService.getAppliancesByFilter(filter, pageable);
    }
}