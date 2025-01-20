package com.onetool.server.blueprint.service;

import com.onetool.server.blueprint.Blueprint;
import com.onetool.server.blueprint.InspectionStatus;
import com.onetool.server.blueprint.repository.BlueprintRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        List<Blueprint> approvedBlueprints = blueprintRepository.findByInspectionStatus(InspectionStatus.PASSED);

        refreshBlueprints(approvedBlueprints);
    }

    @Transactional
    private void refreshBlueprints(List<Blueprint> blueprints) {
        for (Blueprint blueprint : blueprints) {
            log.info("Refreshing blueprint: {}", blueprint.getId());
        }

        //TODO 필요한 로직 추가
    }
}