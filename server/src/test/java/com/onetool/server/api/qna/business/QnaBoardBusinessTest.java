package com.onetool.server.api.qna.business;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.dto.request.PostQnaBoardRequest;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.api.qna.dto.response.QnaBoardDetailResponse;
import com.onetool.server.api.qna.service.QnaBoardService;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.MemberErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.onetool.server.api.fixture.MemberFixture.*;
import static com.onetool.server.api.fixture.QnaBoardFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QnaBoardBusinessTest {

    @Mock
    private QnaBoardService qnaBoardService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private QnaBoardBusiness qnaBoardBusiness;

    @Test
    void 모든_질문들를_조회후_QnaBoardBriefResponse리스트로_반환한다() {
        // ✅ Given (설정)
        Member memberA = createMember(1L);
        Member memberB = createMember(2L);

        List<QnaBoard> qnaBoardsA = createQnaBoards(List.of(1L, 2L), memberA);
        List<QnaBoard> qnaBoardsB = createQnaBoards(List.of(3L, 4L), memberB);
        List<QnaBoard> mergeQnaBoards = mergeQnaBoards(qnaBoardsA, qnaBoardsB);

        when(qnaBoardService.findAll()).thenReturn(mergeQnaBoards);

        // ✅ When (실행)
        List<QnaBoardBriefResponse> resultQnaList = qnaBoardBusiness.getQnaBoardBriefList();

        // ✅ Then (검증)
        assertThat(resultQnaList.size()).isEqualTo(4);
        assertThat(resultQnaList.get(0).title()).isEqualTo("Title1");
        assertThat(resultQnaList.get(3).title()).isEqualTo("Title4");
    }

    @Test
    void 특정_유저의_모든_질문을_조회한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        List<QnaBoard> qnaBoards = createQnaBoards(List.of(1L, 2L), member);

        when(qnaBoardService.findAll(any(Long.class))).thenReturn(qnaBoards);
        doNothing().when(memberService).validateExistId(1L);

        // ✅ When (실행)
        List<QnaBoardBriefResponse> resultQnaBoards = qnaBoardBusiness.getMyQna(1L);

        // ✅ Then (검증)
        assertThat(resultQnaBoards.size()).isEqualTo(2);
        assertThat(resultQnaBoards.get(0).title()).isEqualTo("Title1");
        assertThat(resultQnaBoards.get(1).title()).isEqualTo("Title2");
    }

    @Test
    void 특정유저가_존재하지_않으면_질문조회를_실패한다() {
        // ✅ Given (설정)
        doThrow(new ApiException(MemberErrorCode.NON_EXIST_USER, "ID가 일치하는 회원이 존재하지 않습니다."))
                .when(memberService).validateExistId(1L);

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaBoardBusiness.getMyQna(1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("ID가 일치하는 회원이 존재하지 않습니다.");
    }

    @Test
        //아.... 테스트 어케하냐 이거 도와줘 제발
    void 응답을_생성한다() {
        // ✅ Given (설정)
        String email = "user1@example.com";
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L);
        PostQnaBoardRequest postQnaBoardRequest = mock(PostQnaBoardRequest.class);

        when(memberService.findOne(email)).thenReturn(member);
        when(postQnaBoardRequest.toQnaBoard()).thenReturn(qnaBoard);

        // ✅ When (실행)
        qnaBoardBusiness.createQnaBoard(email, postQnaBoardRequest);

        // ✅ Then (검증)
        verify(qnaBoardService).saveQnaBoard(member, qnaBoard);
    }

    @Test
    void 특정_질문을_조회한다() {
        // ✅ Given (설정)
        String email = "user1@example.com";
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);

        when(memberService.findOne(email)).thenReturn(member);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);

        // ✅ When (실행)
        QnaBoardDetailResponse resultQnaBoard = qnaBoardBusiness.getQnaBoardDetail(email, 1L);

        // ✅ Then (검증)
        assertThat(resultQnaBoard.title()).isEqualTo("Title1");
        assertThat(resultQnaBoard.writer()).isEqualTo("User1");
    }

    @Test
    void 조회한_특정질문이_수정권한이_있다() {
        // ✅ Given (설정)
        String email = "user1@example.com";
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);

        when(memberService.findOne(email)).thenReturn(member);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);

        // ✅ When (실행)
        QnaBoardDetailResponse resultQnaBoard = qnaBoardBusiness.getQnaBoardDetail(email, 1L);

        // ✅ Then (검증)
        assertThat(resultQnaBoard.authorization()).isEqualTo(true);
    }

    @Test
    void 조회한_특정질문이_수정권한이_없다() {
        // ✅ Given (설정)
        String emailMemberB = "user2@example.com";
        Member memberA = createMember(1L);
        Member memberB = createMember(2L);
        QnaBoard qnaBoard = createQnaBoard(1L, memberA);

        when(memberService.findOne(emailMemberB)).thenReturn(memberB);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);

        // ✅ When (실행)
        QnaBoardDetailResponse resultQnaBoard = qnaBoardBusiness.getQnaBoardDetail(emailMemberB, 1L);

        // ✅ Then (검증)
        assertThat(resultQnaBoard.authorization()).isEqualTo(false);
    }

    @Test
    void 특정_질문을_삭제한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);

        when(memberService.findOne("user1@example.com")).thenReturn(member);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);
        // ✅ When (실행)
        qnaBoardBusiness.removeQnaBoard("user1@example.com", 1L);

        // ✅ Then (검증)
        verify(qnaBoardService, times(1)).deleteQnaBoard(qnaBoard, member);
    }

    @Test
    void 삭제_권한이_없으면_질문_삭제를_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        Member cantModifyMember = createMember(2L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);

        when(memberService.findOne("user2@example.com")).thenReturn(cantModifyMember);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);
        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaBoardBusiness.removeQnaBoard("user2@example.com", 1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("해당 유저는 삭제 및 수정 권한이 없습니다.");
    }

    @Test
    void 특정_질문을_갱신한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        PostQnaBoardRequest postQnaBoardRequest = mock(PostQnaBoardRequest.class);

        when(memberService.findOne("user1@example.com")).thenReturn(member);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);

        // ✅ When (실행)
        qnaBoardBusiness.editQnaBoard("user1@example.com", 1L, postQnaBoardRequest);

        // ✅ Then (검증)
        verify(qnaBoardService, times(1)).updateQnaBoard(qnaBoard, postQnaBoardRequest);
    }

    @Test
    void 수정_권한이_없으면_갱신작업을_실패한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        Member cantModifyMember = createMember(2L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);

        when(memberService.findOne("user2@example.com")).thenReturn(cantModifyMember);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);
        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> qnaBoardBusiness.removeQnaBoard("user2@example.com", 1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("해당 유저는 삭제 및 수정 권한이 없습니다.");
    }
}