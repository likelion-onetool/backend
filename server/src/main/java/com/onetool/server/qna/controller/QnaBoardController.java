package com.onetool.server.qna.controller;

import com.onetool.server.global.exception.BaseResponse;
import com.onetool.server.qna.service.QnaBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import static com.onetool.server.qna.dto.request.QnaBoardRequest.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaService;

    /**
     * 문의사항 게시판 첫 화면
     * 게시글들이 최신순으로 제목, 작성자, 작성 시간만 보임
     * @return
     */
    @GetMapping("/qna")
    public BaseResponse<?> qnaHome(){
        return BaseResponse.onSuccess(qnaService.getQnaBoard());
    }

    /**
     * 문의사항 글 작성
     * @param principal
     * @param request
     * @return
     */
    @PostMapping("/qna/post")
    public BaseResponse<?> qnaWrite(Principal principal, @Valid @RequestBody PostQnaBoard request){
        qnaService.postQna(principal, request);
        return BaseResponse.onSuccess("문의사항 등록이 완료됐습니다.");
    }

    /**
     * 문의사항 글 자세히 보기
     * @param qnaId
     * @return
     */
    @GetMapping("/qna/{qnaId}")
    public BaseResponse<?> qnaDetails(Principal principal, @PathVariable Long qnaId){
        return BaseResponse.onSuccess(qnaService.getQnaBoardDetails(principal, qnaId));
    }

    /**
     * 문의사항 삭제
     * @param principal
     * @param qnaId
     * @return
     */
    @PostMapping("/qna/{qnaId}/delete")
    public BaseResponse<?> qnaDelete(Principal principal, @PathVariable Long qnaId){
        qnaService.deleteQna(principal, qnaId);
        return BaseResponse.onSuccess("게시글이 삭제되었습니다.");
    }

    //TODO : 게시글 수정 방법 : 게시글 상세 페이지 -> 게시글 수정 클릭 -> 수정 페이지 -> 수정 완료

    @GetMapping("/qna/{qnaId}/update")
    public BaseResponse<?> getQnaToUpdate(Principal principal, @PathVariable Long qnaId){
        return BaseResponse.onSuccess("게시글이 수정되었습니다.");
    }

    /**
     * 문의사항 게시글 수정
     *
     * @param principal
     * @param qnaId
     * @param request
     * @return
     */
    @PostMapping("/qna/{qnaId}/update")
    public BaseResponse<?> qnaUpdate(Principal principal, @PathVariable Long qnaId, @Valid @RequestBody PostQnaBoard request){
        qnaService.updateQna(principal, qnaId, request);
        return BaseResponse.onSuccess("게시글이 수정되었습니다.");
    }
}