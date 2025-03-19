package com.onetool.server.api.blueprint.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.global.exception.BlueprintNotFoundException;
import com.onetool.server.global.exception.BlueprintNullPointException;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.BlueprintErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
public class BlueprintService {

    private final BlueprintRepository blueprintRepository;

    public List<Blueprint> findAllBlueprintByIds(Set<Long> blueprintIds) {
        List<Blueprint> blueprintList = blueprintRepository.findAllById(blueprintIds);
        if (blueprintList.size() != blueprintIds.size()) {
            throw new ApiException(BlueprintErrorCode.NO_BLUEPRINT_FOUND,"특정 id가 blueprint에 없습니다.");
        }
        return blueprintList;
    }

    public List<Blueprint> findAll(){
        List<Blueprint> blueprintList = blueprintRepository.findAll();
        return blueprintList;
    }

    public Blueprint findBlueprintById(Long blueprintId) {
        return blueprintRepository.findById(blueprintId)
                .orElseThrow(() -> new ApiException(BlueprintErrorCode.NOT_FOUND_ERROR,"blueprintId : " + blueprintId));
    }

    public void saveBlueprint(Blueprint blueprint) {
        validateBlueprintIsNull(blueprint);
        blueprintRepository.save(blueprint);
    }

    public void updateBlueprint(Blueprint blueprint, BlueprintResponse blueprintResponse) {
        validateBlueprintIsNull(blueprint);
        blueprint.updateBlueprint(blueprintResponse);
    }

    public void deleteBlueprint(Blueprint blueprint) {
        validateBlueprintIsNull(blueprint);
        blueprintRepository.delete(blueprint);
    }

    private void validateBlueprintIsNull(Blueprint blueprint) {
        if (blueprint == null) {
            throw new ApiException(BlueprintErrorCode.NULL_POINT_ERROR,"blueprint가 NULL입니다.");
        }
    }
}
