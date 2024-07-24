package com.onetool.server.qna.controller;

import com.onetool.server.global.exception.BaseResponse;
import com.onetool.server.member.domain.CustomUserDetails;
import com.onetool.server.qna.security.AuthUser;
import com.onetool.server.qna.service.QnaReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.onetool.server.qna.dto.request.QnaReplyRequest.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/qna/{qnaId}")
public class QnaReplyController {

    private final QnaReplyService qnaReplyService;

    @PostMapping("/reply")
    public BaseResponse<?> addReply(@AuthUser CustomUserDetails user,
                                    @PathVariable Long qnaId,
                                    @Valid @RequestBody PostQnaReply request){
        qnaReplyService.postReply(user.getContext(), qnaId, request);
        return BaseResponse.onSuccess("댓글이 등록됐습니다.");
    }

    @DeleteMapping("/reply")
    public BaseResponse<?> deleteReply(@AuthUser CustomUserDetails user,
                                       @PathVariable Long qnaId,
                                       @Valid @RequestBody ModifyQnaReply request){
        qnaReplyService.deleteReply(user.getContext(), qnaId, request);
        return BaseResponse.onSuccess("댓글이 삭제됐습니다.");
    }

    @PatchMapping("/reply")
    public BaseResponse<?> updateReply(@AuthUser CustomUserDetails user,
                                       @PathVariable Long qnaId,
                                       @Valid @RequestBody ModifyQnaReply request){
        qnaReplyService.updateReply(user.getContext(), qnaId, request);
        return BaseResponse.onSuccess("댓글이 수정됐습니다.");
    }
}