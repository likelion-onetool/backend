package com.onetool.server.api.qna.dto.response;

import com.onetool.server.api.qna.QnaReply;
import lombok.Builder;

import java.time.LocalDateTime;

public class QnaReplyResponse {

    @Builder
    public record QnaDetailsReplyResponse(
            String content,
            String writer,
            LocalDateTime writtenTime
    ){
        public static QnaDetailsReplyResponse from(QnaReply qnaReply){
            return QnaDetailsReplyResponse.builder()
                    .content(qnaReply.getContent())
                    .writer(qnaReply.getWriter())
                    .writtenTime(qnaReply.getUpdatedAt())
                    .build();
        }
    }
}