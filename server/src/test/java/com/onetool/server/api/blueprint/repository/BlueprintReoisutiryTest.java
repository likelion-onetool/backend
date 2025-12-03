package com.onetool.server.api.blueprint.repository;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class BlueprintReoisutiryTest {

    private final BlueprintRepository blueprintRepository;
    private final EntityManager entityManager;

    @Autowired
    public BlueprintReoisutiryTest(BlueprintRepository blueprintRepository, EntityManager entityManager) {
        this.blueprintRepository = blueprintRepository;
        this.entityManager = entityManager;
    }


    @DisplayName("키워드 검색 시, N+1 문제가 발생하지 않는다")
    @Test
    @Transactional
    void keyword_n_plus_1_test() {
        // given
        Pageable pageable = PageRequest.of(0, 20);

        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // when
        Page<Blueprint> blueprints = blueprintRepository.findAllNameAndCreatorContaining("마을", InspectionStatus.PASSED, pageable);

        for (Blueprint blueprint : blueprints.getContent()) {
            blueprint.getOrderBlueprints().size();
        }

        // then
        long queryCount = statistics.getPrepareStatementCount();
        System.out.println("Executed queries: " + queryCount);

        assertThat(queryCount).as("N+1 문제가 발생했습니다. 예상 쿼리 수: 3, 실제 쿼리 수: " + queryCount).isEqualTo(3L);
    }
}
