package com.onetool.server.api.qna.controller;

import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.api.qna.dto.request.PostQnaBoardRequest;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.api.qna.dto.response.QnaBoardDetailResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Q&A 게시판", description = "Q&A 게시판 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/qna")
public class QnaBoardController {

    private final QnaBoardBusiness qnaBoardBusiness;

    @Operation(summary = "Q&A 게시글 목록 조회 API", description = "전체 Q&A 게시글 목록을 간략하게 조회합니다.")
    @GetMapping("/list")
    public ApiResponse<List<QnaBoardBriefResponse>> qnaHome() {
        return ApiResponse.onSuccess(qnaBoardBusiness.getQnaBoardBriefList());
    }

    @Operation(summary = "Q&A 게시글 작성 API", description = "새로운 Q&A 게시글을 작성합니다.")
    @PostMapping("/post")
    public ApiResponse<String> qnaWrite(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody PostQnaBoardRequest request) {
        log.info("쓰기");
        qnaBoardBusiness.createQnaBoard(principalDetails.getContext().getEmail(), request);
        return ApiResponse.onSuccess("문의사항 등록이 완료됐습니다.");
    }

    @Operation(summary = "Q&A 게시글 상세 조회 API", description = "특정 Q&A 게시글의 상세 내용을 조회합니다.")
    @GetMapping("/{qnaId}")
    public ApiResponse<QnaBoardDetailResponse> qnaDetails(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId) {
        return ApiResponse.onSuccess(qnaBoardBusiness.getQnaBoardDetail(principalDetails.getContext().getEmail(), qnaId));
    }

    @Operation(summary = "Q&A 게시글 삭제 API", description = "특정 Q&A 게시글을 삭제합니다.")
    @DeleteMapping("/{qnaId}")
    public ApiResponse<String> qnaDelete(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId) {
        qnaBoardBusiness.removeQnaBoard(principalDetails.getContext().getEmail(), qnaId);
        return ApiResponse.onSuccess("게시글이 삭제되었습니다.");
    }

    @Operation(summary = "Q&A 게시글 수정 API", description = "특정 Q&A 게시글을 수정합니다.")
    @PatchMapping("/{qnaId}")
    public ApiResponse<String> qnaUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @Valid @RequestBody PostQnaBoardRequest request) {

        qnaBoardBusiness.editQnaBoard(principalDetails.getContext().getEmail(), qnaId, request);
        return ApiResponse.onSuccess("게시글이 수정되었습니다.");
    }
}
