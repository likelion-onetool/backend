package com.onetool.server.api.qna.service;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.qna.dto.request.QnaReplyRequest;

import java.security.Principal;


public interface QnaReplyService {
    void postReply(Principal principal, Long qnaId, QnaReplyRequest.PostQnaReply request);
    void deleteReply(Principal principal, Long qnaId, QnaReplyRequest.ModifyQnaReply request);
    void updateReply(Principal principal, Long qnaId, QnaReplyRequest.ModifyQnaReply request);
    Member findMember(String email);
}
