package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.ApplianceFacade;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplianceControllerTest {

    @Mock
    private ApplianceFacade applianceFacade;

    private ApplianceController controller;

    @BeforeEach
    void setUp() {
        controller = new ApplianceController(applianceFacade);
    }

    @Test
    void getAll_shouldReturnOkAndPageOfAppliances() {
        ApplianceDto applianceDto = ApplianceDto.builder().id(1L).build();
        Page<ApplianceDto> page = new PageImpl<>(List.of(applianceDto));

        when(applianceFacade.getAll(any(ApplianceFilterDto.class), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<ApplianceDto>> response = controller.getAll(new ApplianceFilterDto(), Pageable.unpaged());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(applianceFacade).getAll(any(ApplianceFilterDto.class), any(Pageable.class));
    }

    @Test
    void getById_shouldReturnOkAndAppliance() {
        Long id = 1L;
        ApplianceDto applianceDto = ApplianceDto.builder().id(id).build();

        when(applianceFacade.getById(id)).thenReturn(applianceDto);

        ResponseEntity<ApplianceDto> response = controller.getById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(applianceDto);
        verify(applianceFacade).getById(id);
    }
}
