package com.onetool.server.api.blueprint.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.success.BlueprintDeleteSuccess;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.fixture.BlueprintFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class BlueprintBusinessTest {

    @InjectMocks
    private BlueprintBusiness blueprintBusiness;

    @Mock
    private BlueprintService blueprintService;

    @Test
    void 도면을_생성한다() {
        // given
        BlueprintRequest request = BlueprintFixture.createBlueprintRequest();
        Blueprint blueprint = Blueprint.fromRequest(request);

        when(blueprintService.saveBlueprint(any(Blueprint.class)))
                .thenReturn(blueprint);

        // when
        Blueprint savedBlueprint = blueprintBusiness.createBlueprint(request);

        // then
        assertThat(savedBlueprint.getBlueprintName()).isEqualTo("대한민국 마을");
    }

    @Test
    void 도면의_정보_중_도면이름과_작가이름을_수정한다() {
        // given
        final long id = 1L;

        Blueprint blueprint = BlueprintFixture.createBlueprint();
        BlueprintUpdateRequest request = BlueprintFixture.createBlueprintUpdateRequest();
        BlueprintUpdateSuccess success = BlueprintUpdateSuccess.builder().blueprintId(id).isSuccess(true).build();

        when(blueprintService.findBlueprintById(any(Long.class))).thenReturn(blueprint);
        when(blueprintService.updateBlueprint(any(Blueprint.class), any(BlueprintUpdateRequest.class)))
                .thenReturn(success);

        // when
        BlueprintUpdateSuccess blueprintUpdateSuccess = blueprintBusiness.editBlueprint(request);

        // then
        assertThat(blueprintUpdateSuccess.blueprintId()).isEqualTo(request.id());
        assertThat(blueprintUpdateSuccess.isSuccess()).isEqualTo(true);
    }

    @Test
    void 도면ID를_통해_도면을_삭제한다() {
        // given
        final long id = 1L;

        Blueprint blueprint = BlueprintFixture.createBlueprint();
        when(blueprintService.findBlueprintById(any(Long.class))).thenReturn(blueprint);
        doNothing().when(blueprintService).deleteBlueprint(any(Blueprint.class));

        // when
        BlueprintDeleteSuccess success = blueprintBusiness.removeBlueprint(id);

        // then
        assertThat(success.isSuccess()).isEqualTo(true);
    }
}