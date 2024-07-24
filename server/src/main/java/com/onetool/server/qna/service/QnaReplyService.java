package com.onetool.server.qna.service;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.member.Member;
import com.onetool.server.qna.dto.request.QnaReplyRequest;


public interface QnaReplyService {
    void postReply(MemberAuthContext user, Long qnaId, QnaReplyRequest.PostQnaReply request);
    void deleteReply(MemberAuthContext user, Long qnaId, QnaReplyRequest.ModifyQnaReply request);
    void updateReply(MemberAuthContext user, Long qnaId, QnaReplyRequest.ModifyQnaReply request);
    Member findMember(String email);
}
