package com.onetool.server.api.blueprint.controller;
import com.onetool.server.api.blueprint.dto.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.BlueprintSortRequest;
import com.onetool.server.global.exception.codes.ErrorCode;
import org.springframework.data.domain.Pageable;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blueprint")
public class BlueprintController {

    @Autowired
    private BlueprintService blueprintService;

    @PostMapping("/upload")
    public ApiResponse<String> createBlueprint(@RequestBody BlueprintRequest blueprintRequest,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails == null) {
            throw new MemberNotFoundException(ErrorCode.NON_EXIST_USER);
        }

        blueprintService.createBlueprint(blueprintRequest);
        return ApiResponse.onSuccess("상품이 정상적으로 등록되었습니다.");
    }

    @PutMapping("/update")
    public ApiResponse<String> updateBlueprint(@RequestBody BlueprintResponse blueprintResponse,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails == null) {
            throw new MemberNotFoundException(ErrorCode.NON_EXIST_USER);
        }

        blueprintService.updateBlueprint(blueprintResponse);
        return ApiResponse.onSuccess("상품이 정상적으로 수정 되었습니다.");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteBlueprint(@PathVariable("id") Long id,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails == null) {
            throw new MemberNotFoundException(ErrorCode.NON_EXIST_USER);
        }

        blueprintService.deleteBlueprint(id);
        return ApiResponse.onSuccess("상품이 정상적으로 삭제 되었습니다.");
    }

}