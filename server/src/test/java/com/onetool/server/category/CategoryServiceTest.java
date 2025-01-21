package com.onetool.server.category;

import com.onetool.server.blueprint.service.BlueprintService;
import com.onetool.server.blueprint.dto.SearchResponse;
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
@ActiveProfiles("test")
public class CategoryServiceTest {

    @Autowired
    private BlueprintService blueprintService;

    @DisplayName("building 카테고리를 가진 도면이 잘 나오는지 확인")
    @Test
    void search_first_category_building() {
        FirstCategoryType type = FirstCategoryType.CATEGORY_BUILDING;

        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.findAllByCategory(type, null, pageable);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("building 카테고리의 세부 카테고리의 도면을 검색한다.")
    @Test
    void search_second_category_building() {
        FirstCategoryType type = FirstCategoryType.CATEGORY_BUILDING;
        String second = "주거";

        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.findAllByCategory(type, second, pageable);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("civil 카테고리의 세부 카테고리의 도면을 검색한다.")
    @Test
    void search_second_category_civil() {
        FirstCategoryType type = FirstCategoryType.CATEGORY_CIVIL;
        String second = "공공";

        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.findAllByCategory(type, second, pageable);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("interior 카테고리의 세부 카테고리의 도면을 검색한다.")
    @Test
    void search_second_category_interior() {
        FirstCategoryType type = FirstCategoryType.CATEGORY_INTERIOR;
        String second = "도로";

        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.findAllByCategory(type, second, pageable);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }
    @DisplayName("civil 카테고리의 세부 카테고리의 도면을 검색한다.")
    @Test
    void search_first_category_interior() {
        FirstCategoryType type = FirstCategoryType.CATEGORY_INTERIOR;

        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.findAllByCategory(type, null, pageable);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

}