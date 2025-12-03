package com.onetool.server.api.qna.business;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import com.onetool.server.api.qna.dto.request.ModifyQnaReplyRequest;
import com.onetool.server.api.qna.dto.request.PostQnaReplyRequest;
import com.onetool.server.api.qna.service.QnaBoardService;
import com.onetool.server.api.qna.service.QnaReplyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.onetool.server.api.fixture.MemberFixture.*;
import static com.onetool.server.api.fixture.QnaBoardFixture.*;
import static com.onetool.server.api.fixture.QnaReplyFixture.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QnaReplyBusinessTest {

    @Mock
    private QnaReplyService qnaReplyService;
    @Mock
    private QnaBoardService qnaBoardService;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private QnaReplyBusiness qnaReplyBusiness;

    @Test
    void 응답을_생성한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createEmptyQnaReply();
        PostQnaReplyRequest request = mock(PostQnaReplyRequest.class);

        when(memberService.findOne("user1@example.com")).thenReturn(member);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);
        when(request.fromRequestTooQnaReply(request)).thenReturn(qnaReply);

        // ✅ When (실행)
        qnaReplyBusiness.createQnaReply("user1@example.com",1L,request);

        // ✅ Then (검증)
        verify(qnaReplyService, times(1)).saveQnaReply(member, qnaBoard, qnaReply);
    }

    @Test
    void 응답을_삭제한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);

        when(memberService.findOne("user1@example.com")).thenReturn(member);
        when(qnaBoardService.fetchWithQnaReply(1L)).thenReturn(qnaBoard);
        when(qnaReplyService.fetchWithBoardAndMember(1L)).thenReturn(qnaReply);

        // ✅ When (실행)
        qnaReplyBusiness.removeQnaReply("user1@example.com",1L, 1L);

        // ✅ Then (검증)
        verify(qnaReplyService, times(1)).deleteQnaReply(member, qnaBoard, qnaReply);
    }

    @Test
    void 응답을_갱신한다() {
        // ✅ Given (설정)
        Member member = createMember(1L);
        QnaBoard qnaBoard = createQnaBoard(1L, member);
        QnaReply qnaReply = createQnaReply(1L, member, qnaBoard);
        ModifyQnaReplyRequest request = mock(ModifyQnaReplyRequest.class);

        when(memberService.findOne("user1@example.com")).thenReturn(member);
        when(request.content()).thenReturn("테스트수정응답");
        when(qnaReplyService.fetchWithBoardAndMember(1L)).thenReturn(qnaReply);

        // ✅ When (실행)
        qnaReplyBusiness.updateQnaReply("user1@example.com",1L, 1L, request);

        // ✅ Then (검증)
        verify(qnaReplyService,times(1)).updateQnaReply(member,request.content(),qnaReply);
    }
}
