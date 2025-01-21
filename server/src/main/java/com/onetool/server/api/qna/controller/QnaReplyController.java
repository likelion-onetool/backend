package com.onetool.server.api.qna.controller;

import com.onetool.server.api.qna.dto.request.QnaReplyRequest;
import com.onetool.server.api.qna.service.QnaReplyService;
import com.onetool.server.global.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/qna/{qnaId}")
public class QnaReplyController {

    private final QnaReplyService qnaReplyService;

    @PostMapping("/reply")
    public ApiResponse<String> addReply(Principal principal,
                                   @PathVariable Long qnaId,
                                   @Valid @RequestBody QnaReplyRequest.PostQnaReply request){
        qnaReplyService.postReply(principal, qnaId, request);
        return ApiResponse.onSuccess("댓글이 등록됐습니다.");
    }

    @DeleteMapping("/reply")
    public ApiResponse<String> deleteReply(Principal principal,
                                      @PathVariable Long qnaId,
                                      @Valid @RequestBody QnaReplyRequest.ModifyQnaReply request){
        qnaReplyService.deleteReply(principal, qnaId, request);
        return ApiResponse.onSuccess("댓글이 삭제됐습니다.");
    }

    @PatchMapping("/reply")
    public ApiResponse<String> updateReply(Principal principal,
                                      @PathVariable Long qnaId,
                                      @Valid @RequestBody QnaReplyRequest.ModifyQnaReply request){
        qnaReplyService.updateReply(principal, qnaId, request);
        return ApiResponse.onSuccess("댓글이 수정됐습니다.");
    }
}