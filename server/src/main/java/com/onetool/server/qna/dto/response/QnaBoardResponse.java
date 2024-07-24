package com.onetool.server.qna.dto.response;

import com.onetool.server.qna.QnaBoard;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

import static com.onetool.server.qna.dto.response.QnaReplyResponse.*;

public class QnaBoardResponse {

    @Builder
    public record QnaBoardBriefResponse(
            String title,
            String writer,
            LocalDate postDate,
            Long views,
            Integer replies
    ){
        public static QnaBoardBriefResponse brief(QnaBoard qnaBoard){
            return new QnaBoardBriefResponse(
                    qnaBoard.getTitle(),
                    qnaBoard.getMember().getName(),
                    qnaBoard.getCreatedAt().toLocalDate(),
                    qnaBoard.getViews(),
                    qnaBoard.getQnaReplies().size());
        }

    }

    @Builder
    public record QnaBoardDetailResponse(
            String title,
            String content,
            String writer,
            LocalDate postDate,
            List<QnaDetailsReplyResponse> replies,
            boolean authorization
    ){
        public static QnaBoardDetailResponse details(QnaBoard qnaBoard, boolean hasAuthorization){
            return new QnaBoardDetailResponse(
                    qnaBoard.getTitle(),
                    qnaBoard.getMember().getName(),
                    qnaBoard.getContent(),
                    qnaBoard.getCreatedAt().toLocalDate(),
                    qnaBoard.getQnaReplies()
                            .stream()
                            .map(QnaDetailsReplyResponse::entityToDto)
                            .toList(),
                    hasAuthorization);
        }

    }
}
