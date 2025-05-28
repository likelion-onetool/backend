package com.onetool.server.api.blueprint.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.success.BlueprintDeleteSuccess;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.global.annotation.Business;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class BlueprintBusiness {

    private final BlueprintService blueprintService;

    @Transactional
    public Blueprint createBlueprint(BlueprintRequest blueprintRequest) {
        Blueprint blueprint = Blueprint.fromRequest(blueprintRequest);
        return blueprintService.saveBlueprint(blueprint);
    }

    @Transactional
    public BlueprintUpdateSuccess editBlueprint(BlueprintUpdateRequest request) {
        Blueprint blueprint = blueprintService.findBlueprintById(request.id());
        return blueprintService.updateBlueprint(blueprint, request);
    }

    @Transactional
    public BlueprintDeleteSuccess removeBlueprint(Long blueprintId) {
        Blueprint blueprint = blueprintService.findBlueprintById(blueprintId);
        blueprintService.deleteBlueprint(blueprint);
        return BlueprintDeleteSuccess.builder().isSuccess(true).build();
    }
}
