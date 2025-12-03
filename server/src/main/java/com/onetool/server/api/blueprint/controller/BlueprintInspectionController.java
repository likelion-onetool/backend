package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.business.BlueprintInspectionBusiness;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.success.BlueprintDeleteSuccess;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 - 도면 검수", description = "관리자의 도면 검수 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BlueprintInspectionController {

    private final BlueprintInspectionBusiness blueprintInspectionBusiness;

    @Operation(summary = "미승인 도면 목록 조회 API", description = "승인되지 않은 도면 목록을 페이지별로 조회합니다.")
    @GetMapping("/inspection")
    public ApiResponse<List<BlueprintResponse>> getInspection(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<BlueprintResponse> blueprintResponseList = blueprintInspectionBusiness.getNotPassedBlueprintList(pageable);
        return ApiResponse.onSuccess(blueprintResponseList);
    }

    @Operation(summary = "도면 승인 API", description = "특정 도면을 승인 처리합니다.")
    @PostMapping("/inspection")
    public ApiResponse<?> postInspection(@RequestBody Long id) {
        BlueprintUpdateSuccess success = blueprintInspectionBusiness.editBlueprintWithApprove(id);
        return ApiResponse.onSuccess("승인이 완료되었습니다");
    }

    @Operation(summary = "도면 반려(삭제) API", description = "특정 도면을 반려(삭제) 처리합니다.")
    @DeleteMapping("/inspection")
    public ApiResponse<?> deleteInspection(@RequestBody Long id) {
        BlueprintDeleteSuccess success = blueprintInspectionBusiness.removeBlueprint(id);
        return ApiResponse.onSuccess("반려(삭제)가 완료되었습니다.");
    }
}
