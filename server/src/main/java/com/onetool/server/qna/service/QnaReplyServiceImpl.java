package com.onetool.server.qna.service;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.global.handler.MyExceptionHandler;
import com.onetool.server.member.Member;
import com.onetool.server.member.MemberRepository;
import com.onetool.server.qna.QnaBoard;
import com.onetool.server.qna.QnaReply;
import com.onetool.server.qna.repository.QnaBoardRepository;
import com.onetool.server.qna.repository.QnaReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import static com.onetool.server.global.exception.codes.ErrorCode.*;
import static com.onetool.server.qna.dto.request.QnaReplyRequest.*;

@Service
@RequiredArgsConstructor
public class QnaReplyServiceImpl implements QnaReplyService{

    private final MemberRepository memberRepository;
    private final QnaBoardRepository qnaBoardRepository;
    private final QnaReplyRepository qnaReplyRepository;

    public void postReply(MemberAuthContext user,
                          Long qnaId,
                          PostQnaReply request){

        Member member = findMember(user.getEmail());
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        QnaReply qnaReply = QnaReply.createReply(request);

        qnaReply.addReplyToBoard(qnaBoard);
        qnaReply.addReplyToWriter(member);
        qnaReplyRepository.save(qnaReply);

    }

    public void deleteReply(MemberAuthContext user,
                            Long qnaId,
                            ModifyQnaReply request){
        QnaReply qnaReply = findQnaReply(request.replyId());
        findMemberWithReply(qnaReply, user.getEmail());
        qnaReply.deleteReply();
    }

    public void updateReply(MemberAuthContext user,
                            Long qnaId,
                            ModifyQnaReply request){
        QnaReply qnaReply = findQnaReply(request.replyId());
        findMemberWithReply(qnaReply, user.getEmail());
        qnaReply.updateReply(request.content());
    }

    public void findMemberWithReply(QnaReply qnaReply, String userEmail){
        if(qnaReply.getMember().getEmail().equals(userEmail))
            throw new BaseException(UNAVAILABLE_TO_MODIFY);
    }

    public Member findMember(String email) {
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new MyExceptionHandler(NON_EXIST_USER));
    }

    public QnaBoard findQnaBoard(Long id){
        return qnaBoardRepository
                .findByIdWithReplies(id)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
    }

    public QnaReply findQnaReply(Long id){
        return qnaReplyRepository
                .findByIdWithBoardAndMember(id)
                .orElseThrow(() -> new BaseException(NO_QNA_REPLY));
    }
}
