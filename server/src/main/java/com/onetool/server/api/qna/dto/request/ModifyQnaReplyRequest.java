package com.onetool.server.api.qna.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ModifyQnaReplyRequest(
        @NotBlank(message = "수정할 댓글 내용이 비어있을 수 없습니다.")
        String content
){}
