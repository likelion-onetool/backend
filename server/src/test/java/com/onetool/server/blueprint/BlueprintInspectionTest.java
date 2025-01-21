package com.onetool.server.blueprint;

import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BlueprintInspectionTest {

    private static final Logger log = LoggerFactory.getLogger(BlueprintInspectionTest.class);
    private BlueprintInspectionService blueprintInspectionService;

    @Autowired
    public BlueprintInspectionTest(BlueprintInspectionService blueprintInspectionService) {
        this.blueprintInspectionService = blueprintInspectionService;
    }

    @Test
    @DisplayName("검증이 필요한 도면들을 불러오고, 그 중 한 개를 승인합니다.")
    void get_and_approve_bluprint_test() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        log.info("{}", blueprintInspectionService.findAllNotPassedBlueprints(pageable).size());

        // when
        blueprintInspectionService.approveBlueprint(1L);

        // then
        assertThat(
                blueprintInspectionService.findAllNotPassedBlueprints(pageable).size()
        ).isEqualTo(1);
    }

    @Test
    @DisplayName("관리자 회원만 인터셉터를 통과하여 /damin/* 경로에 접근이 가능하다.")
    void test_admin_interceptor() {

    }
}