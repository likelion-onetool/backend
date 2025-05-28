package com.onetool.server.api.blueprint.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.success.BlueprintDeleteSuccess;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import com.onetool.server.api.fixture.BlueprintFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class BlueprintInspectionBusinessTest {

    @InjectMocks
    private BlueprintInspectionBusiness business;

    @Mock
    private BlueprintInspectionService service;

    @Test
    void 승인되지_않은_도면들을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        when(service.findAllNotPassedBlueprintsWithPage(any(Pageable.class)))
                .thenReturn(BlueprintFixture.createBlueprintPage());

        // when
        List<BlueprintResponse> responses = business.getNotPassedBlueprintList(pageable);

        // then
        assertThat(responses).hasSize(3);
    }

    @Test
    void 승인되지_않은_도면의_검증상태를_통과로_변경한다() {
        // given
        final long blueprintId = 1L;
        Blueprint blueprint = BlueprintFixture.createBlueprint();

        when(service.findBluePrintById(any(Long.class)))
                .thenReturn(blueprint);
        when(service.updateBlueprintInspectionStatus(any(Blueprint.class)))
                .thenReturn(BlueprintUpdateSuccess.of(true, blueprintId));

        // when
        BlueprintUpdateSuccess success = business.editBlueprintWithApprove(blueprintId);

        // then
        assertThat(success.isSuccess()).isEqualTo(true);
        assertThat(success.blueprintId()).isEqualTo(blueprintId);
    }

    @Test
    void 승인되지_않은_도면을_반려처리_한다() {
        // given
        final long blueprintId = 1L;
        when(service.deleteBlueprintById(any(Long.class)))
                .thenReturn(BlueprintDeleteSuccess.of(true));

        // when
        BlueprintDeleteSuccess success = business.removeBlueprint(blueprintId);

        // then
        assertThat(success.isSuccess()).isEqualTo(true);
    }
}
