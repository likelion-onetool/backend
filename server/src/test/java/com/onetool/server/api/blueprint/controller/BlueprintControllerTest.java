package com.onetool.server.api.blueprint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.api.fixture.BlueprintFixture;
import com.onetool.server.api.helper.MockBeanInjection;
import com.onetool.server.global.handler.ExceptionAdvice;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(ExceptionAdvice.class)
@WebMvcTest({BlueprintInspectionController.class, BlueprintController.class, SearchController.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles({"test"})
public class BlueprintControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 새로운_blueprint를_생성한다() throws Exception {
        // given
        BlueprintRequest request = BlueprintFixture.createBlueprintRequest();
        doNothing().when(blueprintBusiness).createBlueprint(any(BlueprintRequest.class));

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/blueprint/upload")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void blueprint의_이름을_변경한다() throws Exception {
        // given
        BlueprintUpdateRequest request = BlueprintFixture.createBlueprintUpdateRequest();

        // when
        ResultActions resultActions =
                mockMvc.perform(put("/blueprint/update")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("상품이 정상적으로 수정 되었습니다."));
    }

    @Test
    public void blueprint를_삭제한다() throws Exception {
        // given
        final long blueprintId = 3L;

        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/blueprint/delete/" + blueprintId)
                        .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("상품이 정상적으로 삭제 되었습니다."));
    }
}
