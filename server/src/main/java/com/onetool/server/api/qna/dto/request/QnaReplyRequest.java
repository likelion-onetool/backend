package com.onetool.server.api.qna.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class QnaReplyRequest {
    public record PostQnaReply(
            @Size(min = 2, max = 100, message = "내용은 2 ~ 100자 이여야 합니다.")
            String content
    ){}

    public record ModifyQnaReply(
            @NotNull
            Long replyId,
            String content
    ){}
}