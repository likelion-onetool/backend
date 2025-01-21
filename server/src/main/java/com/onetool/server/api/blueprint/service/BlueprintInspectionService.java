package com.onetool.server.api.blueprint.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.blueprint.InspectionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<BlueprintResponse> findAllNotPassedBlueprints(Pageable pageable){
        Page<Blueprint> blueprints = blueprintRepository.findByInspectionStatus(InspectionStatus.NONE, pageable);

        return blueprints.stream()
                .map(BlueprintResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveBlueprint(Long id) {
        blueprintRepository.findById(id).ifPresent(Blueprint::approveBlueprint);
    }

    @Transactional
    public void rejectBlueprint(Long id) {
        blueprintRepository.deleteById(id);
    }
}