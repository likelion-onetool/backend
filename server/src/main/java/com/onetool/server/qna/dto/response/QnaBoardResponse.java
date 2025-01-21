package com.onetool.server.qna.dto.response;

import com.onetool.server.qna.QnaBoard;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.onetool.server.qna.dto.response.QnaReplyResponse.QnaDetailsReplyResponse;

public class QnaBoardResponse {

    @Builder
    public record QnaBoardBriefResponse(
            String title,
            String writer,
            LocalDate postDate,
            Long views,
            Integer replies
    ){
        public static QnaBoardBriefResponse from(QnaBoard qnaBoard){
            return QnaBoardBriefResponse.builder()
                    .title(qnaBoard.getTitle())
                    .writer(qnaBoard.getTitle())
                    .postDate(qnaBoard.getCreatedAt().toLocalDate())
                    .views(qnaBoard.getViews())
                    .replies(qnaBoard.getQnaReplies().size())
                    .build();
        }
        public static List<QnaBoardBriefResponse> from(List<QnaBoard> qnaBoards){
            return qnaBoards.stream()
                    .map(QnaBoardBriefResponse::from)
                    .collect(Collectors.toList());
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
        public static QnaBoardDetailResponse from(QnaBoard qnaBoard, boolean hasAuthorization){
            return QnaBoardDetailResponse.builder()
                    .title(qnaBoard.getTitle())
                    .content(qnaBoard.getContent())
                    .writer(qnaBoard.getMember().getName())
                    .postDate(qnaBoard.getCreatedAt().toLocalDate())
                    .replies(qnaBoard.getQnaReplies()
                            .stream()
                            .map(QnaDetailsReplyResponse::from)
                            .toList())
                    .authorization(hasAuthorization)
                    .build();
        }

    }
}
