package com.onetool.server.qna.service;

import com.onetool.server.global.handler.MyExceptionHandler;
import com.onetool.server.member.Member;
import com.onetool.server.member.MemberRepository;
import com.onetool.server.qna.QnaBoard;
import com.onetool.server.qna.repository.QnaBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.onetool.server.global.exception.codes.ErrorCode.*;
import static com.onetool.server.qna.dto.response.QnaBoardResponse.*;
import static com.onetool.server.qna.dto.request.QnaBoardRequest.*;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardServiceImpl implements QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<QnaBoardBriefResponse> getQnaBoard() {
        List<QnaBoard> qnaBoards = qnaBoardRepository
                .findAllQnaBoardsOrderedByCreatedAt();
        hasErrorWithNoContent(qnaBoards);

        //TODO : 페이징 관련
        return qnaBoards
                .stream()
                .map(QnaBoardBriefResponse::brief)
                .toList();
    }

    public void postQna(Principal principal, PostQnaBoard request) {
        Member member = findMember(principal);
        QnaBoard qnaBoard = QnaBoard.createQnaBoard(request);
        qnaBoard.post(member);
        qnaBoardRepository.save(qnaBoard);
    }

    @Transactional
    public QnaBoardDetailResponse getQnaBoardDetails(Principal principal, Long qnaId) {
        Member member = findMember(principal);
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        return QnaBoardDetailResponse.details(qnaBoard,
                isMemberAvailableToModifyQna(qnaBoard, member));
    }

    public void deleteQna(Principal principal, Long qnaId) {
        Member member = findMember(principal);
        QnaBoard qnaBoard = findQnaBoard(qnaId);
        isMemberAvailableToModifyQna(qnaBoard, member);
        qnaBoard.delete(member);
        qnaBoardRepository.delete(qnaBoard);
    }

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
            throw new MyExceptionHandler(NO_QNA_CONTENT);
    }

    public QnaBoard findQnaBoard(Long qnaId){
        return qnaBoardRepository
                .findByIdWithReplies(qnaId)
                .orElseThrow(() -> new MyExceptionHandler(NO_QNA_CONTENT));
    }

    public Member findMember(Principal principal){
        return memberRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new MyExceptionHandler(NON_EXIST_USER));
    }
    public boolean isMemberAvailableToModifyQna(QnaBoard qnaBoard, Member member){
        return qnaBoard.getMember().getEmail().equals(member.getEmail());
    }
}
