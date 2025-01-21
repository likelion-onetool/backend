package com.onetool.server.api.blueprint.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.global.exception.BlueprintNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class BlueprintService {
    private final BlueprintRepository blueprintRepository;

    public BlueprintService(BlueprintRepository blueprintRepository) {
        this.blueprintRepository = blueprintRepository;
    }

    public void createBlueprint(final BlueprintRequest blueprintRequest) {
        Blueprint blueprint = convertToBlueprint(blueprintRequest);
        blueprintRepository.save(blueprint);
    }

    public void updateBlueprint(BlueprintResponse blueprintResponse) {
        Blueprint existingBlueprint = blueprintRepository.findById(blueprintResponse.id())
                .orElseThrow(BlueprintNotFoundException::new);

        Blueprint updatedBlueprint = updateExistingBlueprint(existingBlueprint, blueprintResponse);
        blueprintRepository.save(updatedBlueprint);
    }

    public void deleteBlueprint(Long id) {
        blueprintRepository.findById(id)
                .orElseThrow(BlueprintNotFoundException::new);

        blueprintRepository.deleteById(id);
    }

    private Blueprint convertToBlueprint(final BlueprintRequest blueprintRequest) {
        return Blueprint.fromRequest(blueprintRequest);
    }

    private Blueprint updateExistingBlueprint(Blueprint existingBlueprint, BlueprintResponse blueprintResponse) {
        return Blueprint.builder()
                .id(existingBlueprint.getId())
                .blueprintName(blueprintResponse.blueprintName())
                .categoryId(blueprintResponse.categoryId())
                .standardPrice(blueprintResponse.standardPrice())
                .blueprintImg(blueprintResponse.blueprintImg())
                .blueprintDetails(blueprintResponse.blueprintDetails())
                .extension(blueprintResponse.extension())
                .program(blueprintResponse.program())
                .hits(blueprintResponse.hits())
                .salePrice(blueprintResponse.salePrice())
                .saleExpiredDate(blueprintResponse.saleExpiredDate())
                .creatorName(blueprintResponse.creatorName())
                .downloadLink(blueprintResponse.downloadLink())
                .build();
    }
}