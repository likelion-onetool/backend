package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.business.BlueprintInspectionBusiness;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.success.BlueprintDeleteSuccess;
import com.onetool.server.api.blueprint.dto.success.BlueprintUpdateSuccess;
import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BlueprintInspectionController {

    private final BlueprintInspectionBusiness blueprintInspectionBusiness;

    @GetMapping("/inspection")
    public ApiResponse<List<BlueprintResponse>> getInspection(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<BlueprintResponse> blueprintResponseList = blueprintInspectionBusiness.getNotPassedBlueprintList(pageable);
        return ApiResponse.onSuccess(blueprintResponseList);
    }

    @PostMapping("/inspection")
    public ApiResponse<?> postInspection(@RequestBody Long id) {
        BlueprintUpdateSuccess success = blueprintInspectionBusiness.editBlueprintWithApprove(id);
        return ApiResponse.onSuccess("승인이 완료되었습니다");
    }

    @DeleteMapping("/inspection")
    public ApiResponse<?> deleteInspection(@RequestBody Long id) {
        BlueprintDeleteSuccess success = blueprintInspectionBusiness.removeBlueprint(id);
        return ApiResponse.onSuccess("반려(삭제)가 완료되었습니다.");
    }
}
