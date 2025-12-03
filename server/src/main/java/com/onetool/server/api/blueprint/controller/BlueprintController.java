package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.business.BlueprintBusiness;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "도면", description = "도면 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
public class BlueprintController {

    private final BlueprintBusiness blueprintBusiness;

    @Operation(summary = "도면 업로드 API", description = "새로운 도면을 업로드합니다.")
    @PostMapping("/upload")
    public ApiResponse<String> postBlueprint(BlueprintRequest request) {
        Blueprint blueprint = blueprintBusiness.createBlueprint(request);
        return ApiResponse.onSuccess("상품이 정상적으로 등록되었습니다.");
    }

    @Operation(summary = "도면 정보 수정 API", description = "기존 도면의 정보를 수정합니다.")
    @PutMapping("/update")
    public ApiResponse<String> putBlueprint(BlueprintUpdateRequest request) {
        BlueprintUpdateSuccess command = blueprintBusiness.editBlueprint(request);
        return ApiResponse.onSuccess("상품이 정상적으로 수정 되었습니다.");
    }

    @Operation(summary = "도면 삭제 API", description = "특정 도면을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteBlueprint(@PathVariable("id") Long id) {
        blueprintBusiness.removeBlueprint(id);
        return ApiResponse.onSuccess("상품이 정상적으로 삭제 되었습니다.");
    }
}
