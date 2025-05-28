package com.onetool.server.api.qna.service;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import com.onetool.server.api.qna.dto.request.PostQnaBoardRequest;
import com.onetool.server.api.qna.repository.QnaBoardRepository;
import com.onetool.server.global.new_exception.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.onetool.server.api.fixture.MemberFixture.*;
import static com.onetool.server.api.fixture.QnaBoardFixture.*;
import static com.onetool.server.api.fixture.QnaReplyFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QnaBoardServiceTest {

    @Mock
    private QnaBoardRepository qnaBoardRepository;

    @InjectMocks
    private QnaBoardService qnaBoardService;

    @Test
    void 서버_전체_QnaBoard를_조회한다() {

        // ✅ Given (설정)
        List<Member> members = createMembers(List.of(1L, 2L, 3L));
        QnaBoard qna1 = createQnaBoard(1L, members.get(0));
        QnaBoard qna2 = createQnaBoard(2L, members.get(0));
        QnaBoard qna3 = createQnaBoard(3L, members.get(1));
        QnaBoard qna4 = createQnaBoard(4L, members.get(2));

        List<QnaBoard> mockQnaBoards = List.of(qna1, qna2, qna3, qna4);

        when(qnaBoardRepository.findAllQnaBoardsOrderedByCreatedAt()).thenReturn(mockQnaBoards);

        // ✅ When (실행)
        List<QnaBoard> result = qnaBoardService.findAll();

        // ✅ Then (검증)
        assertThat(result.size()).isEqualTo(4);
        assertIterableEquals(mockQnaBoards, result);
    }

    @Test
    void 서버_전체_QnaBoard가_없으면_에러를_발생한다() {
        // ✅ Given (설정)
        List<QnaBoard> mockQnaBoard = new ArrayList<>();
        when(qnaBoardRepository.findAllQnaBoardsOrderedByCreatedAt()).thenReturn(mockQnaBoard);

        // ✅ When (실행)
        // ✅ Then (검증)
        assertThatThrownBy(()->qnaBoardService.findAll())
                .isInstanceOf(ApiException.class)
                .hasMessage("해당 객체는 서버에 존재하지 않습니다");
    }

    @Test
    void 답변이_있는_특정_질문게시판을_정상적으로_조회한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);

        when(qnaBoardRepository.findByIdWithReplies(1L)).thenReturn(Optional.of(qnaBoard));

        // ✅ When (실행)
        QnaBoard findQnaBoard = qnaBoardService.fetchWithQnaReply(1L);

        // ✅ Then (검증)
        assertThat(findQnaBoard.getContent()).isEqualTo("content1");
        assertThat(findQnaBoard.getMember().getName()).isEqualTo("User1");
        assertThat(findQnaBoard.getQnaReplies().get(0).getContent()).isEqualTo("테스트응답1");
    }
    @Test
    void 답변이_있는_특정_질문게시판이_없으면_에러를_반환한다() {
        // ✅ Given (설정)
        when(qnaBoardRepository.findByIdWithReplies(1L)).thenReturn(Optional.empty());

        // ✅ When (실행)
        // ✅ Then (검증)
        assertThatThrownBy(()->qnaBoardService.fetchWithQnaReply(1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("해당 객체는 서버에 존재하지 않습니다");
    }

    @Test
    void 특정_사용자가_만든_모든_질문을_조회한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        List<QnaBoard> mockQnaBoards = createQnaBoards(List.of(1L, 2L), member);
        when(qnaBoardRepository.findByMemberId(member.getId())).thenReturn(mockQnaBoards);

        // ✅ When (실행)
        List<QnaBoard> findQnaBoards = qnaBoardService.findAll(member.getId());

        // ✅ Then (검증)
        assertThat(findQnaBoards.size()).isEqualTo(2);
        assertIterableEquals(findQnaBoards,mockQnaBoards);
    }

    @Test
    void 질문을_저장한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L);

        // ✅ When (실행)
        qnaBoardService.saveQnaBoard(member,qnaBoard);

        // ✅ Then (검증)
        assertThat(qnaBoard.getMember()).isEqualTo(member);
    }

    @Test
    void 질문이_NULL이면_저장을_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = null;

        // ✅ When (실행) & Then (검증)
        assertThatThrownBy(()-> qnaBoardService.saveQnaBoard(member,qnaBoard))
                .isInstanceOf(ApiException.class)
                .hasMessage("qnaBoard가 NULL입니다.");

    }

    @Test
    void 질문을_삭제한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = mock(QnaBoard.class);

        doNothing().when(qnaBoardRepository).delete(qnaBoard);

        // ✅ When (실행)
        qnaBoardService.deleteQnaBoard(qnaBoard, member);

        // ✅ Then (검증)
        verify(qnaBoard).unassignMember(member);
        verify(qnaBoardRepository).delete(qnaBoard);
    }

    @Test
    void 질문이_NULL이면_삭제를_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = null;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(()-> qnaBoardService.deleteQnaBoard(qnaBoard,member))
                .isInstanceOf(ApiException.class)
                .hasMessage("qnaBoard가 NULL입니다.");
    }

    @Test
    void 질문을_요청에_맞게_갱신한다() {
        // ✅ Given (설정)
        PostQnaBoardRequest postQnaBoardRequest = mock(PostQnaBoardRequest.class);
        when(postQnaBoardRequest.title()).thenReturn("테스트제목");
        when(postQnaBoardRequest.content()).thenReturn("테스트답변");

        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);

        // ✅ When (실행)
        qnaBoardService.updateQnaBoard(qnaBoard,postQnaBoardRequest);

        // ✅ Then (검증)
        assertThat("테스트제목").isEqualTo(qnaBoard.getTitle());
        assertThat("테스트답변").isEqualTo(qnaBoard.getContent());
    }

    @Test
    void 질문이_NULL이면_갱신작업을_실패한다() {
        // ✅ Given (설정)
        PostQnaBoardRequest postQnaBoardRequest = mock(PostQnaBoardRequest.class);
        QnaBoard qnaBoard = null;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(()->qnaBoardService.updateQnaBoard(qnaBoard,postQnaBoardRequest))
                .isInstanceOf(ApiException.class)
                .hasMessage("qnaBoard가 NULL입니다.");
    }

}