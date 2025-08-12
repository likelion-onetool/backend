package com.onetool.server.api.qna.controller;

import com.onetool.server.api.qna.business.QnaReplyBusiness;
import com.onetool.server.api.qna.dto.request.ModifyQnaReplyRequest;
import com.onetool.server.api.qna.dto.request.PostQnaReplyRequest;
import com.onetool.server.api.qna.service.QnaReplyService;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/qna/{qnaId}")
public class QnaReplyController {

    private final QnaReplyBusiness qnaReplyBusiness;

    @PostMapping("/reply")
    public ApiResponse<String> addReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @Valid @RequestBody PostQnaReplyRequest request) {

        qnaReplyBusiness.createQnaReply(principalDetails.getContext().getEmail(), qnaId, request);
        return ApiResponse.onSuccess("댓글이 등록됐습니다.");
    }

    @DeleteMapping("/reply")
    public ApiResponse<String> deleteReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @Valid @RequestBody ModifyQnaReplyRequest request) {

        qnaReplyBusiness.removeQnaReply(principalDetails.getContext().getEmail(),qnaId,request);
        return ApiResponse.onSuccess("댓글이 삭제됐습니다.");
    }

    @PatchMapping("/reply")
    public ApiResponse<String> updateReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @Valid @RequestBody ModifyQnaReplyRequest request) {

        qnaReplyBusiness.updateQnaReply(principalDetails.getContext().getEmail(),qnaId,request);
        return ApiResponse.onSuccess("댓글이 수정됐습니다.");
    }
}