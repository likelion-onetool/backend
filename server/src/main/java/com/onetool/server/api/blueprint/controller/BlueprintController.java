package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.business.BlueprintBusiness;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
public class BlueprintController {

    private final BlueprintBusiness blueprintBusiness;

    @PostMapping("/upload")
    public ApiResponse<String> postBlueprint(BlueprintRequest request) {
        Blueprint blueprint = blueprintBusiness.createBlueprint(request);
        return ApiResponse.onSuccess("상품이 정상적으로 등록되었습니다.");
    }

    @PutMapping("/update")
    public ApiResponse<String> putBlueprint(BlueprintUpdateRequest request) {
        BlueprintUpdateSuccess command = blueprintBusiness.editBlueprint(request);
        return ApiResponse.onSuccess("상품이 정상적으로 수정 되었습니다.");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteBlueprint(@PathVariable("id") Long id) {
        blueprintBusiness.removeBlueprint(id);
        return ApiResponse.onSuccess("상품이 정상적으로 삭제 되었습니다.");
    }
}
