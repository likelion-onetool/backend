package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BlueprintInspectionController {

    private final BlueprintInspectionService blueprintInspectionService;

    @GetMapping("/inspection")
    public ApiResponse<List<BlueprintResponse>> getNotPassedBlueprints(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<BlueprintResponse> responses = blueprintInspectionService.findAllNotPassedBlueprints(pageable);
        return ApiResponse.onSuccess(responses);
    }

    @PostMapping("/inspection")
    public ApiResponse<?> approveBlueprint(@RequestBody Long id) {
        blueprintInspectionService.approveBlueprint(id);
        return ApiResponse.onSuccess("승인이 완료되었습니다");
    }

    @DeleteMapping("/inspection")
    public ApiResponse<?> rejectBlueprint(@RequestBody Long id) {
        blueprintInspectionService.rejectBlueprint(id);
        return ApiResponse.onSuccess("반려(삭제)가 완료되었습니다.");
    }
}