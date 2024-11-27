package com.onetool.server.blueprint;

import com.onetool.server.api.blueprint.dto.SearchResponse;
import com.onetool.server.api.blueprint.service.BlueprintService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test","dev"})
public class BlueprintServiceTest {

    @Autowired
    private BlueprintService blueprintService;

    @DisplayName("키워드 기반 검색이 잘되는지 확인")
    @Test
    void search_with_keyword() {
        String keyword = "마을";
        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.searchNameAndCreatorWithKeyword(keyword, pageable);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }
}