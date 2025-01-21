package com.onetool.server.api.blueprint.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BlueprintInspectionService {

    private final BlueprintRepository blueprintRepository;

    @Transactional
    public List<BlueprintResponse> findAllNotPassedBlueprints(){
        List<Blueprint> blueprints = blueprintRepository.findByInspectionStatus(InspectionStatus.NONE);

        return blueprints.stream()
                .map(BlueprintResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveBlueprint(Long id) {
        blueprintRepository.findById(id).ifPresent(Blueprint::approveBlueprint);
    }
}