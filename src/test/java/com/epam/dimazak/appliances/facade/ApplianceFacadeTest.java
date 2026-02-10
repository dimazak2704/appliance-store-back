package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.appliance.ApplianceDto;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import com.epam.dimazak.appliances.service.ApplianceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplianceFacadeTest {

    @Mock
    private ApplianceService applianceService;

    @InjectMocks
    private ApplianceFacade applianceFacade;

    @Test
    void getById_shouldReturnApplianceDto() {
        Long id = 1L;
        ApplianceDto expectedDto = new ApplianceDto();
        expectedDto.setId(id);

        when(applianceService.getApplianceById(id)).thenReturn(expectedDto);

        ApplianceDto result = applianceFacade.getById(id);

        assertThat(result).isEqualTo(expectedDto);
        verify(applianceService).getApplianceById(id);
    }

    @Test
    void getAll_shouldReturnPageOfApplianceDtos() {
        ApplianceFilterDto filter = new ApplianceFilterDto();
        Pageable pageable = Pageable.unpaged();
        Page<ApplianceDto> expectedPage = new PageImpl<>(Collections.emptyList());

        when(applianceService.getAppliancesByFilter(filter, pageable)).thenReturn(expectedPage);

        Page<ApplianceDto> result = applianceFacade.getAll(filter, pageable);

        assertThat(result).isEqualTo(expectedPage);
        verify(applianceService).getAppliancesByFilter(filter, pageable);
    }
}
