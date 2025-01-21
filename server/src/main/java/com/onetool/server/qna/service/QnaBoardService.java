package com.onetool.server.qna.service;

import com.onetool.server.member.domain.Member;
import com.onetool.server.qna.QnaBoard;
import com.onetool.server.qna.dto.request.QnaBoardRequest;

import java.security.Principal;
import java.util.List;

import static com.onetool.server.qna.dto.response.QnaBoardResponse.QnaBoardBriefResponse;
import static com.onetool.server.qna.dto.response.QnaBoardResponse.QnaBoardDetailResponse;

public interface QnaBoardService {

    List<QnaBoardBriefResponse> getQnaBoard();
    void postQna(Principal principal, QnaBoardRequest.PostQnaBoard request);
    QnaBoardDetailResponse getQnaBoardDetails(Principal principal, Long boardId);
    void deleteQna(Principal principal, Long boardId);
    void updateQna(Principal principal, Long boardId, QnaBoardRequest.PostQnaBoard request);

    //에러 확인 메서드
    QnaBoard findQnaBoard(Long boardId);
    void hasErrorWithNoContent(List<QnaBoard> data);
    Member findMember(Principal principal);
    boolean isMemberAvailableToModifyQna(QnaBoard qnaBoard, Member member);
}
