package com.onetool.server.api.blueprint;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.response.SearchResponse;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles({"test", "local"})
public class BlueprintServiceTest {

    @Autowired
    private BlueprintService blueprintService;

    @Autowired
    private BlueprintSearchService blueprintSearchService;

    @Autowired
    private BlueprintRepository blueprintRepository;

    private Blueprint blueprint;

//    @DisplayName("키워드 기반 검색이 잘되는지 확인")
//    @Test
//    void search_with_keyword() {
//        String keyword = "마을";
//        Pageable pageable = PageRequest.of(0, 5);
//        Page<SearchResponse> response = blueprintSearchService.searchNameAndCreatorWithKeyword(keyword, pageable);
//        assertThat(response.getTotalElements()).isEqualTo(6);
//    }

//    @DisplayName("id가 1인 Blueprint가 삭제 상태로 변경되는지 확인")
//    @Test
//    void testMarkAsDeleted() {
//        // given: Blueprint가 초기화되고 isDeleted는 false
//        Blueprint blueprint = blueprintRepository.findById(1L)
//                .orElseThrow(() -> new IllegalArgumentException("Blueprint with id 1 not found"));
//        assertThat(blueprint.getIsDeleted()).isFalse();
//
//        // when: Blueprint를 삭제 상태로 설정
//        blueprintService.deleteBlueprint(blueprint.getId());
//
//        // then: 삭제 상태가 true로 변경되었는지 확인
//        Blueprint deletedBlueprint = blueprintRepository.findDeletedById(1L)
//                .orElseThrow(() -> new IllegalArgumentException("Blueprint with id 1 not found"));
//
//        assertThat(deletedBlueprint.getIsDeleted()).isTrue();
//    }
}