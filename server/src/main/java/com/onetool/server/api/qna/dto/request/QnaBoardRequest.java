package com.onetool.server.api.qna.dto.request;

import jakarta.validation.constraints.Size;


public class QnaBoardRequest {

    public record PostQnaBoard(
            @Size(min = 2, max = 30, message = "제목은 2 ~ 30자 이여야 합니다.")
            String title,
            @Size(min = 2, max = 100, message = "내용은 2 ~ 100자 이여야 합니다.")
            String content,
            String writer
    ){}
}