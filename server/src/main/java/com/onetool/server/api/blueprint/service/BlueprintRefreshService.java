package com.onetool.server.api.blueprint.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.blueprint.InspectionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlueprintRefreshService {

    private final BlueprintRepository blueprintRepository;

    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정 새로고침
    public void refreshApprovedBlueprints() {
        log.info("Refreshing approved blueprints at midnight...");
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Blueprint> approvedBlueprints = blueprintRepository.findByInspectionStatus(InspectionStatus.PASSED, pageable);

        refreshBlueprints(approvedBlueprints.getContent());
    }

    private void refreshBlueprints(List<Blueprint> blueprints) {
        for (Blueprint blueprint : blueprints) {
            log.info("Refreshing blueprint: {}", blueprint.getId());
        }

        //TODO 필요한 로직 추가
    }
}