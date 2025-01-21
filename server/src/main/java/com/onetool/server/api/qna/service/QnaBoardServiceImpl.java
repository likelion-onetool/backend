package com.onetool.server.api.qna.service;

import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberRepository;
import com.onetool.server.api.qna.repository.QnaBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

import static com.onetool.server.global.exception.codes.ErrorCode.NON_EXIST_USER;
import static com.onetool.server.global.exception.codes.ErrorCode.NO_QNA_CONTENT;
import static com.onetool.server.api.qna.dto.request.QnaBoardRequest.PostQnaBoard;
import static com.onetool.server.api.qna.dto.response.QnaBoardResponse.QnaBoardBriefResponse;
import static com.onetool.server.api.qna.dto.response.QnaBoardResponse.QnaBoardDetailResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaBoardServiceImpl implements QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;
    private final MemberRepository memberRepository;

    public List<QnaBoardBriefResponse> getQnaBoard() {
        List<QnaBoard> qnaBoards = qnaBoardRepository
                .findAllQnaBoardsOrderedByCreatedAt();
        hasErrorWithNoContent(qnaBoards);

        //TODO : 페이징 관련
        return qnaBoards
                .stream()
                .map(QnaBoardBriefResponse::from)
                .toList();
    }

    @Transactional
    public void postQna(Principal principal, PostQnaBoard request) {
        Member member = findMember(principal);
        QnaBoard qnaBoard = QnaBoard.createQnaBoard(request);
        qnaBoard.post(member);
        qnaBoardRepository.save(qnaBoard);
    }

    public QnaBoardDetailResponse getQnaBoardDetails(Principal principal, Long qnaId) {
        Member member = findMember(principal);
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        return QnaBoardDetailResponse.from(qnaBoard,
                isMemberAvailableToModifyQna(qnaBoard, member));
    }

    @Transactional
    public void deleteQna(Principal principal, Long qnaId) {
        Member member = findMember(principal);
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        isMemberAvailableToModifyQna(qnaBoard, member);
        qnaBoard.delete(member);
        qnaBoardRepository.delete(qnaBoard);
    }

    @Transactional
    public void updateQna(Principal principal, Long qnaId, PostQnaBoard request){
        Member member = findMember(principal);
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        isMemberAvailableToModifyQna(qnaBoard, member);
        qnaBoard.updateQnaBoard(request);
        qnaBoardRepository.save(qnaBoard);
    }

    //예외 검증

    public void hasErrorWithNoContent(List<QnaBoard> data) {
        if(data.isEmpty())
            throw new BaseException(NO_QNA_CONTENT);
    }

    public QnaBoard findQnaBoard(Long qnaId){
        return qnaBoardRepository
                .findByIdWithReplies(qnaId)
                .orElseThrow(() -> new BaseException(NO_QNA_CONTENT));
    }

    public Member findMember(Principal principal){
        return memberRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
    }
    public boolean isMemberAvailableToModifyQna(QnaBoard qnaBoard, Member member){
        return qnaBoard.getMember().getEmail().equals(member.getEmail());
    }
}
