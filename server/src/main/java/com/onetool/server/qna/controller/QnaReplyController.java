package com.onetool.server.qna.controller;

import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.qna.service.QnaReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.onetool.server.qna.dto.request.QnaReplyRequest.ModifyQnaReply;
import static com.onetool.server.qna.dto.request.QnaReplyRequest.PostQnaReply;


@RestController
@RequiredArgsConstructor
@RequestMapping("/qna/{qnaId}")
public class QnaReplyController {

    private final QnaReplyService qnaReplyService;

    @PostMapping("/reply")
    public ApiResponse<String> addReply(Principal principal,
                                   @PathVariable Long qnaId,
                                   @Valid @RequestBody PostQnaReply request){
        qnaReplyService.postReply(principal, qnaId, request);
        return ApiResponse.onSuccess("댓글이 등록됐습니다.");
    }

    @DeleteMapping("/reply")
    public ApiResponse<String> deleteReply(Principal principal,
                                      @PathVariable Long qnaId,
                                      @Valid @RequestBody ModifyQnaReply request){
        qnaReplyService.deleteReply(principal, qnaId, request);
        return ApiResponse.onSuccess("댓글이 삭제됐습니다.");
    }

    @PatchMapping("/reply")
    public ApiResponse<String> updateReply(Principal principal,
                                      @PathVariable Long qnaId,
                                      @Valid @RequestBody ModifyQnaReply request){
        qnaReplyService.updateReply(principal, qnaId, request);
        return ApiResponse.onSuccess("댓글이 수정됐습니다.");
    }
}