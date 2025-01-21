package com.onetool.server.api.qna.service;

import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberRepository;
import com.onetool.server.api.qna.repository.QnaBoardRepository;
import com.onetool.server.api.qna.repository.QnaReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.onetool.server.global.exception.codes.ErrorCode.*;
import static com.onetool.server.api.qna.dto.request.QnaReplyRequest.ModifyQnaReply;
import static com.onetool.server.api.qna.dto.request.QnaReplyRequest.PostQnaReply;

@Service
@RequiredArgsConstructor
public class QnaReplyServiceImpl implements QnaReplyService{

    private final MemberRepository memberRepository;
    private final QnaBoardRepository qnaBoardRepository;
    private final QnaReplyRepository qnaReplyRepository;

    public void postReply(Principal principal,
                          Long qnaId,
                          PostQnaReply request){

        Member member = findMember(principal.getName());
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        QnaReply qnaReply = QnaReply.createReply(request);

        qnaReply.addReplyToBoard(qnaBoard);
        qnaReply.addReplyToWriter(member);
        qnaReplyRepository.save(qnaReply);

    }

    public void deleteReply(Principal principal,
                            Long qnaId,
                            ModifyQnaReply request){
        QnaReply qnaReply = findQnaReply(request.replyId());
        findMemberWithReply(qnaReply, principal.getName());
        qnaReply.deleteReply();
    }

    public void updateReply(Principal principal,
                            Long qnaId,
                            ModifyQnaReply request){
        QnaReply qnaReply = findQnaReply(request.replyId());
        findMemberWithReply(qnaReply,principal.getName());
        qnaReply.updateReply(request.content());
    }

    public void findMemberWithReply(QnaReply qnaReply, String userEmail){
        if(qnaReply.getMember().getEmail().equals(userEmail))
            throw new BaseException(UNAVAILABLE_TO_MODIFY);
    }

    public Member findMember(String email) {
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
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
