package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.fixture.BlueprintFixture;
import com.onetool.server.api.helper.MockBeanInjection;
import com.onetool.server.api.member.fixture.WithMockPrincipalDetails;
import com.onetool.server.global.handler.ExceptionAdvice;
import com.onetool.server.global.interceptor.AdminInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(ExceptionAdvice.class)
@WebMvcTest({BlueprintInspectionController.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles({"test"})
public class BlueprintInspectionControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AdminInterceptor interceptor;

    @BeforeEach
    void initTest() throws Exception {
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void 승인되지_않은_도면들을_조회한다() throws Exception {
        // given
        when(blueprintInspectionBusiness.getNotPassedBlueprintList(any(Pageable.class)))
                .thenReturn(BlueprintFixture.createBlueprintResponseList());

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/admin/inspection")
                        .with(csrf())
                );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.size()").value(4));
    }

    @Test
    void 승인되지_않은_도면을_승인처리_한다() throws Exception {
        // given
        final long id = 1L;
        doNothing().when(blueprintInspectionBusiness).editBlueprintWithApprove(id);

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/admin/inspection")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(id))
                );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("승인이 완료되었습니다"));
    }

    @Test
    void 승인되지_않은_도면을_반려처리_한다() throws Exception {
        // given
        final long id = 1L;
        doNothing().when(blueprintInspectionBusiness).removeBlueprint(id);

        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/admin/inspection")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(id))
                );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("반려(삭제)가 완료되었습니다."));
    }
}
