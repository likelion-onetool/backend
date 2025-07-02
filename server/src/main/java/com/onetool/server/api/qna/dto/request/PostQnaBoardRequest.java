package com.onetool.server.api.qna.dto.request;

import com.onetool.server.api.qna.QnaBoard;
import jakarta.validation.constraints.Size;


public record PostQnaBoardRequest(
        @Size(min = 2, max = 30, message = "제목은 2 ~ 30자 이여야 합니다.")
        String title,
        @Size(min = 2, max = 100, message = "내용은 2 ~ 100자 이여야 합니다.")
        String content,
        String writer
) {
        public QnaBoard toQnaBoard() {
                return new QnaBoard(title, content);
        }
}