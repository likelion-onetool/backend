package com.onetool.server.blueprint;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.dto.SearchResponse;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
import com.onetool.server.api.blueprint.service.BlueprintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles({"test","dev"})
public class BlueprintServiceTest {

    @Autowired
    private BlueprintService blueprintService;

    @Autowired
    private BlueprintSearchService blueprintSearchService;

    @Autowired
    private BlueprintRepository blueprintRepository;

    private Blueprint blueprint;

    @BeforeEach
    void setUp() {
        // Blueprint 객체를 초기화하고
        blueprint = Blueprint.builder()
                .id(1L)
                .blueprintName("Test Blueprint")
                .categoryId(1L)
                .standardPrice(100L)
                .blueprintImg("image.jpg")
                .blueprintDetails("Details about blueprint")
                .extension("pdf")
                .program("AutoCAD")
                .hits(BigInteger.valueOf(10))
                .salePrice(50L)
                .saleExpiredDate(LocalDateTime.now().plusDays(7))
                .creatorName("Creator")
                .downloadLink("http://example.com")
                .secondCategory("Architecture")
                .inspectionStatus(InspectionStatus.NONE)
                .build();
    }

    @DisplayName("키워드 기반 검색이 잘되는지 확인")
    @Test
    void search_with_keyword() {
        String keyword = "마을";
        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintSearchService.searchNameAndCreatorWithKeyword(keyword, pageable);
        assertThat(response.getTotalElements()).isEqualTo(6);
    }

    @DisplayName("id가 1인 Blueprint가 삭제 상태로 변경되는지 확인")
    @Test
    void testMarkAsDeleted() {
        Blueprint blueprint = blueprintRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Blueprint with id 1 not found"));

        // given: Blueprint가 초기화되고 isDeleted는 false
        assertThat(blueprint.getIsDeleted()).isFalse();

        // when: Blueprint를 삭제 상태로 설정
        blueprintService.deleteBlueprint(blueprint.getId());

        // then: 삭제 상태가 true로 변경되었는지 확인
        Blueprint updatedBlueprint = blueprintRepository.findById(blueprint.getId())
                .orElseThrow(() -> new IllegalArgumentException("Blueprint not found"));

        assertTrue(updatedBlueprint.getIsDeleted(), "Blueprint의 isDeleted 값이 true여야 합니다.");
    }

}