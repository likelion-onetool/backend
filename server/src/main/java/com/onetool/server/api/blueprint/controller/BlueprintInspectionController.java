package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.service.BlueprintInspectionService;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BlueprintInspectionController {

    private final BlueprintInspectionService blueprintInspectionService;

    @GetMapping("/inspection")
    public ApiResponse<List<BlueprintResponse>> getNotPassedBlueprints() {
        List<BlueprintResponse> responses = blueprintInspectionService.findAllNotPassedBlueprints();
        return ApiResponse.onSuccess(responses);
    }

    @PostMapping("/inspection")
    public ApiResponse<?> approveBluprint(@RequestBody Long id) {
        blueprintInspectionService.approveBlueprint(id);
        return ApiResponse.onSuccess("승인이 완료되었습니다");
    }

   /* @DeleteMapping("/inspection")
    public ApiResponse<?> rejectBluprint(@RequestBody Long id) {

    }*/
}
