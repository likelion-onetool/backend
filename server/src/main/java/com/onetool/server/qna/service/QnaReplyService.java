package com.onetool.server.qna.service;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.member.domain.Member;
import com.onetool.server.qna.dto.request.QnaReplyRequest;

import java.security.Principal;


public interface QnaReplyService {
    void postReply(Principal principal, Long qnaId, QnaReplyRequest.PostQnaReply request);
    void deleteReply(Principal principal, Long qnaId, QnaReplyRequest.ModifyQnaReply request);
    void updateReply(Principal principal, Long qnaId, QnaReplyRequest.ModifyQnaReply request);
    Member findMember(String email);
}
