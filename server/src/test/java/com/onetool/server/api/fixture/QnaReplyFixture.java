package com.onetool.server.api.fixture;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import com.onetool.server.api.qna.dto.request.PostQnaReplyRequest;

import java.lang.reflect.Field;

public class QnaReplyFixture {

    private static QnaReply createQnaReplyId(Long id) {
        return QnaReply.builder()
                .content("테스트응답" + id)
                .build();
    }

    public static QnaReply createEmptyQnaReply() {
        return QnaReply.builder()
                .content("테스트응답")
                .build();
    }

    public static QnaReply createQnaReply(Long id, Member member){
        QnaReply qnaReply = createQnaReplyId(id);
        setId(qnaReply, id);
        return qnaReply;
    }

    public static QnaReply createQnaReply(Long id, Member member, QnaBoard qnaBoard) {
        QnaReply qnaReply = createQnaReplyId(id);
        setId(qnaReply, id);
        qnaReply.assignBoard(qnaBoard);
        qnaReply.assignMember(member);
        return qnaReply;
    }

    public static PostQnaReplyRequest createReplyRequest(String text){
        return new PostQnaReplyRequest(text);
    }

    private static void setId(QnaReply qnaReply, Long id) {
        try {
            Field field = QnaReply.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(qnaReply, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set ID via Reflection", e);
        }
    }
}
