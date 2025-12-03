package com.onetool.server.api.qna.business;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import com.onetool.server.api.qna.dto.request.ModifyQnaReplyRequest;
import com.onetool.server.api.qna.dto.request.PostQnaReplyRequest;
import com.onetool.server.api.qna.service.QnaBoardService;
import com.onetool.server.api.qna.service.QnaReplyService;
import com.onetool.server.global.annotation.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class QnaReplyBusiness {

    private final QnaReplyService qnaReplyService;
    private final MemberService memberService;
    private final QnaBoardService qnaBoardService;

    @Transactional
    public void createQnaReply(String email, Long qnaId, PostQnaReplyRequest request) {
        Member member = memberService.findOne(email);
        QnaBoard qnaBoard = qnaBoardService.fetchWithQnaReply(qnaId);
        QnaReply qnaReply = request.fromRequestTooQnaReply(request);
        qnaReplyService.saveQnaReply(member, qnaBoard, qnaReply);
    }

    @Transactional
    public void removeQnaReply(String email, Long qnaId, Long replyId) {
        Member member = memberService.findOne(email);
        QnaBoard qnaBoard = qnaBoardService.fetchWithQnaReply(qnaId);
        QnaReply qnaReply = qnaReplyService.fetchWithBoardAndMember(replyId);
        qnaReplyService.deleteQnaReply(member, qnaBoard, qnaReply);
    }

    @Transactional
    public void updateQnaReply(String email, Long qnaId, Long replyId, ModifyQnaReplyRequest request) {
        Member member = memberService.findOne(email);
        QnaReply qnaReply = qnaReplyService.fetchWithBoardAndMember(replyId);
        qnaReplyService.updateQnaReply(member, request.content(), qnaReply);
    }
}
