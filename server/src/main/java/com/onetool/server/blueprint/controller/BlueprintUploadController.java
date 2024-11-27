package com.onetool.server.blueprint.controller;

import com.onetool.server.blueprint.dto.BlueprintUploadRequest;
import com.onetool.server.blueprint.service.BlueprintS3UploadService;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/blueprint/upload")
public class BlueprintUploadController {

    private final BlueprintS3UploadService blueprintS3UploadService;

    // TODO : validation 추가 (팀원들과 협의 후 결정)
    // 일단 유저 인증은 생략하고 업로드 기능만 구현

    // 도면만 따로 업로드를 하는 api
    @PostMapping("/inspection")
    public ApiResponse<?> postBlueprintFileForInspection(//@AuthenticationPrincipal PrincipalDetails principal,
                                                         @RequestParam("blueprintFiles") List<MultipartFile> blueprintFile) throws IOException {
        return ApiResponse.onSuccess(blueprintS3UploadService.saveFileToInspection(blueprintFile));
    }

}
