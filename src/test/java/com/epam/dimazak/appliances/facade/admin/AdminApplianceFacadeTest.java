package com.epam.dimazak.appliances.facade.admin;

import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import com.epam.dimazak.appliances.service.ApplianceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminApplianceFacadeTest {

    @Mock
    private ApplianceService applianceService;

    @InjectMocks
    private AdminApplianceFacade adminApplianceFacade;

    @Test
    void create_shouldReturnCreatedAppliance() {
        ApplianceRequestDto request = new ApplianceRequestDto();
        ApplianceDto expectedDto = new ApplianceDto();
        when(applianceService.createAppliance(request)).thenReturn(expectedDto);

        ApplianceDto result = adminApplianceFacade.create(request);

        assertThat(result).isEqualTo(expectedDto);
        verify(applianceService).createAppliance(request);
    }

    @Test
    void update_shouldReturnUpdatedAppliance() {
        Long id = 1L;
        ApplianceRequestDto request = new ApplianceRequestDto();
        ApplianceDto expectedDto = new ApplianceDto();
        when(applianceService.updateAppliance(id, request)).thenReturn(expectedDto);

        ApplianceDto result = adminApplianceFacade.update(id, request);

        assertThat(result).isEqualTo(expectedDto);
        verify(applianceService).updateAppliance(id, request);
    }

    @Test
    void delete_shouldCallService() {
        Long id = 1L;
        adminApplianceFacade.delete(id);
        verify(applianceService).deleteAppliance(id);
    }

    @Test
    void uploadImage_shouldReturnUpdatedAppliance() {
        Long id = 1L;
        MultipartFile file = mock(MultipartFile.class);
        ApplianceDto expectedDto = new ApplianceDto();
        when(applianceService.uploadImage(id, file)).thenReturn(expectedDto);

        ApplianceDto result = adminApplianceFacade.uploadImage(id, file);

        assertThat(result).isEqualTo(expectedDto);
        verify(applianceService).uploadImage(id, file);
    }
}
