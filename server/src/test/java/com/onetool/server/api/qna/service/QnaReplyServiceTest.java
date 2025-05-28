package com.onetool.server.api.qna.service;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import com.onetool.server.api.qna.repository.QnaReplyRepository;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.QnaErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.onetool.server.api.fixture.MemberFixture.*;
import static com.onetool.server.api.fixture.QnaBoardFixture.*;
import static com.onetool.server.api.fixture.QnaReplyFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QnaReplyServiceTest {

    @Mock
    private QnaReplyRepository qnaReplyRepository;

    @InjectMocks
    private QnaReplyService qnaReplyService;

    @Test
    void 유저가_작성한_특정_응답을_조회한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);

        when(qnaReplyRepository.findByIdWithBoardAndMember(1L)).thenReturn(Optional.of(qnaReply));

        // ✅ When (실행)
        QnaReply findQnaReply = qnaReplyService.fetchWithBoardAndMember(1L);

        // ✅ Then (검증)
        assertThat(findQnaReply.getId()).isEqualTo(1L);
        assertThat(findQnaReply.getQnaBoard()).isEqualTo(qnaBoard);
        assertThat(findQnaReply.getMember()).isEqualTo(member);
    }

    @Test
    void 특정_응답이_없으면_에러를_반환한다() {
        // ✅ Given (설정)
        when(qnaReplyRepository.findByIdWithBoardAndMember(1L)).thenReturn(Optional.empty());

        // ✅ When (실행) ** Then (검증)
        assertThatThrownBy(() -> qnaReplyService.fetchWithBoardAndMember(1L))
                .isInstanceOf(ApiException.class)
                .hasMessage(QnaErrorCode.NOT_FOUND_ERROR.getDescription());
    }

    @Test
    void 응답을_저장한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member);

        // ✅ When (실행)
        qnaReplyService.saveQnaReply(member, qnaBoard, qnaReply);

        // ✅ Then (검증)
        assertThat(qnaReply.getMember()).isEqualTo(member);
        assertThat(qnaReply.getQnaBoard()).isEqualTo(qnaBoard);
        verify(qnaReplyRepository).save(qnaReply);
    }

    @Test
    void 응답이_NULL이면_저장을_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = null;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaReplyService.saveQnaReply(member, qnaBoard, qnaReply))
                .isInstanceOf(ApiException.class)
                .hasMessage(QnaErrorCode.NULL_POINT_ERROR.getDescription());
    }

    @Test
    void 유저의_응답을_삭제한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);
        doNothing().when(qnaReplyRepository).deleteById(qnaReply.getId());

        // ✅ When (실행)
        qnaReplyService.deleteQnaReply(member, qnaBoard, qnaReply);

        // ✅ Then (검증)
        assertThat(qnaReply.getMember()).isNotSameAs(member);
        assertThat(qnaReply.getQnaBoard()).isNotSameAs(qnaBoard);
        verify(qnaReplyRepository).deleteById(qnaReply.getId());
    }

    @Test
    void 응답이_NULL이면_삭제를_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = null;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaReplyService.deleteQnaReply(member, qnaBoard, qnaReply))
                .isInstanceOf(ApiException.class)
                .hasMessage(QnaErrorCode.NULL_POINT_ERROR.getDescription());
    }

    @Test
    void 유저가_수정삭제_권한이_없으면_응답삭제를_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        Member deleteCantMember = createMember(2L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaReplyService.deleteQnaReply(deleteCantMember, qnaBoard, qnaReply))
                .isInstanceOf(ApiException.class)
                .hasMessage("해당 멤버는 수정/삭제 권한이 없습니다.");
    }

    @Test
    void 유저의_응답을_갱신한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);

        // ✅ When (실행)
        qnaReplyService.updateQnaReply(member, "업데이트테스트", qnaReply);

        // ✅ Then (검증)
        assertThat(qnaReply.getContent()).isEqualTo("업데이트테스트");
    }

    @Test
    void 응답이_NULL이면_업데이트를_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L);
        QnaReply qnaReply = null;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaReplyService.updateQnaReply(member, "업데이트테스트", qnaReply))
                .isInstanceOf(ApiException.class)
                .hasMessage(QnaErrorCode.NULL_POINT_ERROR.getDescription());
    }

    @Test
    void 유저가_수정삭제_권한이_없으면_응답갱신을_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        Member modifyCantMember = createMember(2L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaReplyService.updateQnaReply(modifyCantMember, "업데이트테스트", qnaReply))
                .isInstanceOf(ApiException.class)
                .hasMessage("해당 멤버는 수정/삭제 권한이 없습니다.");
    }
}