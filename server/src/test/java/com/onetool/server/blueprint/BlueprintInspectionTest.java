package com.onetool.server.blueprint;

import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
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
        log.info("{}", blueprintInspectionService.findAllNotPassedBlueprints().size());

        // when
        blueprintInspectionService.approveBlueprint(1L);

        // then
        assertThat(
                blueprintInspectionService.findAllNotPassedBlueprints().size()
        ).isEqualTo(5);

    }
}
