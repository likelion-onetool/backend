package com.onetool.server.api.qna.service;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.dto.request.QnaBoardRequest;
import com.onetool.server.api.qna.dto.response.QnaBoardResponse;

import java.security.Principal;
import java.util.List;

public interface QnaBoardService {

    List<QnaBoardResponse.QnaBoardBriefResponse> getQnaBoard();
    void postQna(Principal principal, QnaBoardRequest.PostQnaBoard request);
    QnaBoardResponse.QnaBoardDetailResponse getQnaBoardDetails(Principal principal, Long boardId);
    void deleteQna(Principal principal, Long boardId);
    void updateQna(Principal principal, Long boardId, QnaBoardRequest.PostQnaBoard request);

    //에러 확인 메서드
    QnaBoard findQnaBoard(Long boardId);
    void hasErrorWithNoContent(List<QnaBoard> data);
    Member findMember(Principal principal);
    boolean isMemberAvailableToModifyQna(QnaBoard qnaBoard, Member member);
}