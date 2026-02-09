package com.epam.dimazak.appliances.facade.admin;

import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import com.epam.dimazak.appliances.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AdminApplianceFacade {

    private final ApplianceService applianceService;

    public ApplianceDto create(ApplianceRequestDto request) {
        return applianceService.createAppliance(request);
    }

    public ApplianceDto update(Long id, ApplianceRequestDto request) {
        return applianceService.updateAppliance(id, request);
    }

    public void delete(Long id) {
        applianceService.deleteAppliance(id);
    }

    public ApplianceDto uploadImage(Long id, MultipartFile file) {
        return applianceService.uploadImage(id, file);
    }
}