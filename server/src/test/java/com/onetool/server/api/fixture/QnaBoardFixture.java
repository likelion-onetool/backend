package com.onetool.server.api.fixture;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.api.qna.dto.response.QnaBoardDetailResponse;
import com.onetool.server.api.qna.dto.response.QnaReplyResponse;
import com.onetool.server.global.entity.BaseEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class QnaBoardFixture {

    private static QnaBoard createQnaBoardById(Long id) {
        return QnaBoard.builder()
                .title("Title"+id)
                .content("content"+id)
                .build();
    }

    public static QnaBoard createQnaBoard(Long id){
        QnaBoard qnaBoard = createQnaBoardById(id);
        setIdAndTimestamps(qnaBoard, id);
        return qnaBoard;
    }

    public static QnaBoard createQnaBoard(Long id, Member member) {
        QnaBoard qnaBoard = createQnaBoardById(id);
        qnaBoard.assignMember(member);
        setIdAndTimestamps(qnaBoard, id); // ID 강제 설정
        return qnaBoard;
    }

    public static QnaBoard createCustomQnaBoard(String title, String content, Member member, Long id) {
        QnaBoard qnaBoard = QnaBoard.builder()
                .title(title)
                .content(content)
                .build();

        if (member != null) {
            qnaBoard.assignMember(member);
        }
        setIdAndTimestamps(qnaBoard, id);
        return qnaBoard;
    }

    public static List<QnaBoard> createQnaBoards(List<Long> ids, Member member){
        return ids.stream()
                .map(id-> createQnaBoard(id,member)).toList();
    }

    @SafeVarargs
    public static List<QnaBoard> mergeQnaBoards(List<QnaBoard>... lists) {
        return Stream.of(lists)
                .flatMap(List::stream)
                .toList();
    }

    public static QnaBoardBriefResponse createQnaBoardBriefResponse() {
        return new QnaBoardBriefResponse("테스트제목","홍길동",LocalDate.now(),100L,1);
    }

    public static QnaBoardDetailResponse createQnaBoardDetailResponse() {
        return new QnaBoardDetailResponse("테스트제목","테스트내용","테스트작가" ,LocalDate.now(),null,true);
    }

    private static void setIdAndTimestamps(QnaBoard qnaBoard, Long id) {
        try {
            // ID 설정
            Field idField = QnaBoard.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(qnaBoard, id);

            // 현재 시간 설정
            LocalDateTime now = LocalDateTime.now();

            // createdAt과 updatedAt 설정
            Field createdField = BaseEntity.class.getDeclaredField("createdAt");
            Field updatedField = BaseEntity.class.getDeclaredField("updatedAt");

            createdField.setAccessible(true);
            updatedField.setAccessible(true);

            createdField.set(qnaBoard, now);
            updatedField.set(qnaBoard, now);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set ID and timestamps via Reflection", e);
        }
    }
}
