package com.onetool.server.api.blueprint.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.success.BlueprintDeleteSuccess;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import com.onetool.server.global.annotation.Business;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Business
@RequiredArgsConstructor
public class BlueprintInspectionBusiness {

    private final BlueprintInspectionService blueprintInspectionService;

    @Transactional
    public List<BlueprintResponse> getNotPassedBlueprintList(Pageable pageable) {
        Page<Blueprint> blueprintPage = blueprintInspectionService.findAllNotPassedBlueprintsWithPage(pageable);
        return BlueprintResponse.fromBlueprintPageToResponseList(blueprintPage);
    }

    @Transactional
    public BlueprintUpdateSuccess editBlueprintWithApprove(Long blueprintId) {
        Blueprint blueprint = blueprintInspectionService.findBluePrintById(blueprintId);
        return blueprintInspectionService.updateBlueprintInspectionStatus(blueprint);
    }

    @Transactional
    public BlueprintDeleteSuccess removeBlueprint(Long blueprintId) {
        return blueprintInspectionService.deleteBlueprintById(blueprintId);
    }
}
