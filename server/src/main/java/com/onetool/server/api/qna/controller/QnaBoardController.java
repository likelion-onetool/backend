package com.onetool.server.api.qna.controller;

import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.api.qna.dto.request.PostQnaBoardRequest;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.api.qna.dto.response.QnaBoardDetailResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QnaBoardController {

    private final QnaBoardBusiness qnaBoardBusiness;

    @GetMapping("/qna/list")
    public ApiResponse<List<QnaBoardBriefResponse>> qnaHome() {
        return ApiResponse.onSuccess(qnaBoardBusiness.getQnaBoardBriefList());
    }

    @PostMapping("/qna/post")
    public ApiResponse<String> qnaWrite(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody PostQnaBoardRequest request) {
        log.info("쓰기");
        qnaBoardBusiness.createQnaBoard(principalDetails.getContext().getEmail(), request);
        return ApiResponse.onSuccess("문의사항 등록이 완료됐습니다.");
    }

    @GetMapping("/qna/{qnaId}")
    public ApiResponse<QnaBoardDetailResponse> qnaDetails(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId) {
        return ApiResponse.onSuccess(qnaBoardBusiness.getQnaBoardDetail(principalDetails.getContext().getEmail(), qnaId));
    }

    @PostMapping("/qna/{qnaId}/delete")
    public ApiResponse<String> qnaDelete(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId) {
        qnaBoardBusiness.removeQnaBoard(principalDetails.getContext().getEmail(), qnaId);
        return ApiResponse.onSuccess("게시글이 삭제되었습니다.");
    }

    //TODO : 게시글 수정 방법 : 게시글 상세 페이지 -> 게시글 수정 클릭 -> 수정 페이지 -> 수정 완료

    @GetMapping("/qna/{qnaId}/update")
    public ApiResponse<String> qnaUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @Valid @RequestBody PostQnaBoardRequest request) {

        qnaBoardBusiness.editQnaBoard(principalDetails.getContext().getEmail(), qnaId, request);
        return ApiResponse.onSuccess("게시글이 수정되었습니다.");
    }
}