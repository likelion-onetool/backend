package com.onetool.server.blueprint.controller;

import com.onetool.server.blueprint.service.BlueprintS3UploadService;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.global.exception.codes.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BlueprintUploadController {

    private final BlueprintS3UploadService blueprintS3UploadService;

    // TODO : validation 추가 (팀원들과 협의 후 결정)
    // 일단 유저 인증은 생략하고 업로드 기능만 구현

    // 도면만 따로 업로드를 하는 api
    @PostMapping("/api/blueprint/upload")
    public ApiResponse<?> postBlueprintFileForInspection(//@AuthenticationPrincipal PrincipalDetails principal,
                                                         @RequestParam("file") MultipartFile multipartFile) throws IOException {
        return ApiResponse.onSuccess(blueprintS3UploadService.saveFileToInspection(multipartFile));
    }

    // 도면 상세 페이지 이미지 업로드
    @PostMapping("/api/blueprint/detail-image/upload")
    public ApiResponse<?> postBlueprintDetailImageForInspection(//@AuthenticationPrincipal PrincipalDetails details,
                                                                @RequestParam("/file") MultipartFile multipartFile) throws IOException {
        return ApiResponse.onSuccess(blueprintS3UploadService.saveFileToDetails(multipartFile));
    }

    // 도면 썸네일 업로드
    // 썸네일에 대한 버킷은 아직 만들지 않음 -> 상세페이지 이미지랑 같이 저장할까 생각 중 (좋은 의견있으면 알려주세요)
    @PostMapping("/api/blueprint/thumbnail/upload")
    public ApiResponse<?> postBlueprintThumbnailForInspection(//@AuthenticationPrincipal PrincipalDetails details,
                                                              @RequestParam("/file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) throw new BaseException(ErrorCode.BLUEPRINT_FILE_NECESSARY);
        return ApiResponse.onSuccess(blueprintS3UploadService.saveFileToDetails(multipartFile));
    }

}
