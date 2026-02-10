package com.epam.dimazak.appliances.controller.admin;

import com.epam.dimazak.appliances.facade.admin.AdminApplianceFacade;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminApplianceControllerTest {

    @Mock
    private AdminApplianceFacade adminApplianceFacade;

    private AdminApplianceController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminApplianceController(adminApplianceFacade);
    }

    @Test
    void create_shouldReturnOkAndApplianceDto() {
        ApplianceRequestDto request = new ApplianceRequestDto();
        ApplianceDto applianceDto = ApplianceDto.builder().id(1L).build();

        when(adminApplianceFacade.create(any(ApplianceRequestDto.class))).thenReturn(applianceDto);

        ResponseEntity<ApplianceDto> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(applianceDto);
        verify(adminApplianceFacade).create(request);
    }

    @Test
    void update_shouldReturnOkAndApplianceDto() {
        Long id = 1L;
        ApplianceRequestDto request = new ApplianceRequestDto();
        ApplianceDto applianceDto = ApplianceDto.builder().id(id).build();

        when(adminApplianceFacade.update(eq(id), any(ApplianceRequestDto.class))).thenReturn(applianceDto);

        ResponseEntity<ApplianceDto> response = controller.update(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(applianceDto);
        verify(adminApplianceFacade).update(id, request);
    }

    @Test
    void delete_shouldReturnOk() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.delete(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(adminApplianceFacade).delete(id);
    }

    @Test
    void uploadImage_shouldReturnOkAndApplianceDto() {
        Long id = 1L;
        MultipartFile file = mock(MultipartFile.class);
        ApplianceDto applianceDto = ApplianceDto.builder().id(id).build();

        when(adminApplianceFacade.uploadImage(id, file)).thenReturn(applianceDto);

        ResponseEntity<ApplianceDto> response = controller.uploadImage(id, file);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(applianceDto);
        verify(adminApplianceFacade).uploadImage(id, file);
    }
}
