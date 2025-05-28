package com.onetool.server.api.blueprint.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.response.SearchResponse;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class BlueprintSearchBusinessTest {

    @InjectMocks
    private BlueprintSearchBusiness business;

    @Mock
    private BlueprintSearchService service;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = mock(Pageable.class);
    }

    @Test
    void 키워드를_통해_도면을_검색한다() {
        // given
        String keyword = "blueprint";
        Blueprint blueprint1 = mock(Blueprint.class);
        Blueprint blueprint2 = mock(Blueprint.class);

        Page<Blueprint> blueprintPage = new PageImpl<>(List.of(blueprint1, blueprint2));

        when(service.findAllByPassed(keyword, pageable)).thenReturn(blueprintPage);
        when(service.fetchAllWithOrderBlueprints(blueprintPage)).thenReturn(List.of(blueprint1, blueprint2));
        when(service.fetchAllWithCartBlueprints(List.of(blueprint1, blueprint2))).thenReturn(List.of(blueprint1, blueprint2));

        Page<SearchResponse> mockPage = mock(Page.class);

        // when
        Page<SearchResponse> result = business.getSearchResponsePage(keyword, pageable);

        // then
        verify(service, times(1)).findAllByPassed(keyword, pageable);
        verify(service, times(1)).fetchAllWithOrderBlueprints(blueprintPage);
        verify(service, times(1)).fetchAllWithCartBlueprints(List.of(blueprint1, blueprint2));
    }
}
