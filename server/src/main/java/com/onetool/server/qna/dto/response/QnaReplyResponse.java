package com.onetool.server.qna.dto.response;

import com.onetool.server.qna.QnaReply;

import java.time.LocalDateTime;

public class QnaReplyResponse {
    public record QnaDetailsReplyResponse(
            String content,
            String writer,
            LocalDateTime writtenTime
    ){
        public static QnaDetailsReplyResponse entityToDto(QnaReply qnaReply){
            return new QnaDetailsReplyResponse(
                    qnaReply.getContent(),
                    qnaReply.getMember().getName(),
                    qnaReply.getUpdatedAt()
            );
        }
    }
}
