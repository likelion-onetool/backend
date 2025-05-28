package com.onetool.server.api.blueprint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.category.FirstCategoryType;
import com.onetool.server.api.fixture.BlueprintFixture;
import com.onetool.server.api.helper.MockBeanInjection;
import com.onetool.server.global.handler.ExceptionAdvice;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(ExceptionAdvice.class)
@WebMvcTest({SearchController.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles({"test"})
public class SearchControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 키워드를_통해_blueprint의_이름을_검색한다() throws Exception {
        // given
        final String keyword = URLEncoder.encode("골프장", StandardCharsets.UTF_8);
        when(blueprintSearchBusiness.getSearchResponsePage(any(String.class), any(Pageable.class)))
                .thenReturn(BlueprintFixture.createSearchResponsePage());

        // when
        ResultActions resultActions = mockMvc.perform(get("/blueprint")
                .param("s", keyword)
                .with(csrf())
        );

        // then
        resultActions
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content[0].blueprintName").value("골프자 1인실 평면도(1)"))
                .andExpect(jsonPath("$.result.content[1].blueprintName").value("골프자 레이아웃 평면도(1)"));
    }

    @Test
    void 키워드를_통해_작가를_검색한다() throws Exception {
        // given
        final String keyword = URLEncoder.encode("원툴", StandardCharsets.UTF_8);
        when(blueprintSearchBusiness.getSearchResponsePage(any(String.class), any(Pageable.class)))
                .thenReturn(BlueprintFixture.createSearchResponsePage());

        // when
        ResultActions resultActions = mockMvc.perform(get("/blueprint")
                .param("s", keyword)
                .with(csrf())
        );

        // then
        resultActions
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content[0].creatorName").value("원툴"))
                .andExpect(jsonPath("$.result.content[1].creatorName").value("원툴"));
    }

    @Test
    void _1차_카테고리가_건축인_도면을_검색한다() throws Exception {
        // given
        when(blueprintSearchBusiness.getSearchResponsePage(any(FirstCategoryType.class), nullable(String.class), any(Pageable.class)))
                .thenReturn(BlueprintFixture.createSearchResponsePage());

        // when
        ResultActions resultActions = mockMvc.perform(get("/blueprint/building")
                .with(csrf()));

        // then
        resultActions
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content[0].categoryId").value(1L))
                .andExpect(jsonPath("$.result.content[1].categoryId").value(1L));
    }

    @Test
    void _1차_카테고리가_건축이면서_2차_카테고리가_공공인_도면을_검색한다() throws Exception {
        // given
        final String expected = "공공";
        final String secondCategory = URLEncoder.encode(expected, StandardCharsets.UTF_8);
        when(blueprintSearchBusiness.getSearchResponsePage(any(FirstCategoryType.class), nullable(String.class), any(Pageable.class)))
                .thenReturn(BlueprintFixture.createSearchResponsePage());

        // when
        ResultActions resultActions = mockMvc.perform(get("/blueprint/building")
                .param("category", secondCategory)
                .with(csrf()));

        // then
        resultActions
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content[0].secondCategory").value(expected))
                .andExpect(jsonPath("$.result.content[1].secondCategory").value(expected));
    }

    @Test
    void 전체_도면을_조회한다() throws Exception {
        // given
        when(blueprintSearchBusiness.getSearchResponsePage(any(Pageable.class)))
                .thenReturn(BlueprintFixture.createSearchResponsePage());

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/blueprint/all")
                        .with(csrf())
                );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.size()").value(11));
    }

    @Test
    void blueprint_id로_상세정보를_조회한다() throws Exception {
        // given
        final long blueprintId = 1L;
        final String blueprintName = "골프장 1인실 평면도(1)";

        when(blueprintSearchBusiness.getApprovedBlueprintResponse(blueprintId))
                .thenReturn(BlueprintFixture.createBlueprintResponse(blueprintId, blueprintName));

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/blueprint/" + blueprintId)
                                .with(csrf()));

        // then
       resultActions
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.result.id").value(blueprintId))
               .andExpect(jsonPath("$.result.blueprintName").value(blueprintName));
    }
}
