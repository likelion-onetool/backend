package com.onetool.server.blueprint.controller;

import com.onetool.server.blueprint.dto.BlueprintUploadRequest;
import com.onetool.server.blueprint.service.BlueprintS3UploadService;
import com.onetool.server.blueprint.service.S3Service;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blueprint/upload")
public class BlueprintUploadController {

    private final BlueprintS3UploadService blueprintS3UploadService;
    private final S3Service s3service;

    // 일단 유저 인증은 생략하고 업로드 기능만 구현
    // 현재 파일을 서버에 업로드하는 방식과 클라이언트에서 업로드 (서버에서 presigned-url 발급)하는 방법 2가지를 구현
    // 어떤 방식을 선택할지는 추후 회의를 통해 결정할 예쩡

    @PostMapping("/inspection")
    public ApiResponse<?> postBlueprintFileForInspection(
            // @AuthenticationPrincipal PrincipalDetails principal,
            @RequestPart("details") BlueprintUploadRequest request,
            @RequestPart("files") List<MultipartFile> files) throws IOException {
        return ApiResponse.onSuccess(blueprintS3UploadService.saveFileToInspection(request, files));
    }

    @GetMapping("/presigned-url")
    public ApiResponse<String> getPresignedUrl(
            // @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam String dir) {
        return ApiResponse.onSuccess(s3service.getPresignedUrl(dir));
    }
}
