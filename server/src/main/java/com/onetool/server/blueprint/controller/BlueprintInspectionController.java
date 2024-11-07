package com.onetool.server.blueprint.controller;

import com.onetool.server.blueprint.dto.BlueprintResponse;
import com.onetool.server.blueprint.service.BlueprintInspectionService;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.codes.ErrorCode;
import com.onetool.server.member.domain.Member;
import com.onetool.server.member.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
