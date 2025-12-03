package com.onetool.server.api.qna.controller;

import com.onetool.server.api.qna.business.QnaReplyBusiness;
import com.onetool.server.api.qna.dto.request.ModifyQnaReplyRequest;
import com.onetool.server.api.qna.dto.request.PostQnaReplyRequest;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Q&A 댓글", description = "Q&A 게시판의 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/qna/{qnaId}/reply")
public class QnaReplyController {

    private final QnaReplyBusiness qnaReplyBusiness;

    @Operation(summary = "Q&A 댓글 작성 API", description = "특정 Q&A 게시글에 댓글을 작성합니다.")
    @PostMapping
    public ApiResponse<String> addReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @Valid @RequestBody PostQnaReplyRequest request) {

        qnaReplyBusiness.createQnaReply(principalDetails.getContext().getEmail(), qnaId, request);
        return ApiResponse.onSuccess("댓글이 등록됐습니다.");
    }

    @Operation(summary = "Q&A 댓글 삭제 API", description = "특정 Q&A 게시글의 댓글을 삭제합니다.")
    @DeleteMapping("/{replyId}")
    public ApiResponse<String> deleteReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @PathVariable(name = "replyId") Long replyId) {

        qnaReplyBusiness.removeQnaReply(principalDetails.getContext().getEmail(), qnaId, replyId);
        return ApiResponse.onSuccess("댓글이 삭제됐습니다.");
    }

    @Operation(summary = "Q&A 댓글 수정 API", description = "특정 Q&A 게시글의 댓글을 수정합니다.")
    @PatchMapping("/{replyId}")
    public ApiResponse<String> updateReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "qnaId") Long qnaId,
            @PathVariable(name = "replyId") Long replyId,
            @Valid @RequestBody ModifyQnaReplyRequest request) {

        qnaReplyBusiness.updateQnaReply(principalDetails.getContext().getEmail(), qnaId, replyId, request);
        return ApiResponse.onSuccess("댓글이 수정됐습니다.");
    }
}
