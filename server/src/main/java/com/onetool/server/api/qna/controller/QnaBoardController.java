package com.onetool.server.api.qna.controller;

import com.onetool.server.api.qna.service.QnaBoardService;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.api.qna.dto.response.QnaBoardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.onetool.server.api.qna.dto.request.QnaBoardRequest.PostQnaBoard;
import static com.onetool.server.api.qna.dto.response.QnaBoardResponse.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    /**
     * 문의사항 게시판 첫 화면
     * 게시글들이 최신순으로 제목, 작성자, 작성 시간만 보임
     * @return
     */
    @GetMapping("/qna/list")
    public ApiResponse<List<QnaBoardResponse.QnaBoardBriefResponse>> qnaHome(){
        return ApiResponse.onSuccess(qnaBoardService.getQnaBoard());
    }

    /**
     * 문의사항 글 작성
     * @param principal
     * @param request
     * @return
     */
    @PostMapping("/qna/post")
    public ApiResponse<String> qnaWrite(Principal principal, @Valid @RequestBody PostQnaBoard request){
        log.info("쓰기");
        qnaBoardService.postQna(principal, request);
        return ApiResponse.onSuccess("문의사항 등록이 완료됐습니다.");
    }

    /**
     * 문의사항 글 자세히 보기
     * @param qnaId
     * @return
     */
    @GetMapping("/{qnaId}")
    public ApiResponse<QnaBoardDetailResponse> qnaDetails(Principal principal, @PathVariable Long qnaId){
        return ApiResponse.onSuccess(qnaBoardService.getQnaBoardDetails(principal, qnaId));
    }

    /**
     * 문의사항 삭제
     * @param principal
     * @param qnaId
     * @return
     */
    @PostMapping("/{qnaId}/delete")
    public ApiResponse<String> qnaDelete(Principal principal, @PathVariable Long qnaId){
        qnaBoardService.deleteQna(principal, qnaId);
        return ApiResponse.onSuccess("게시글이 삭제되었습니다.");
    }

    //TODO : 게시글 수정 방법 : 게시글 상세 페이지 -> 게시글 수정 클릭 -> 수정 페이지 -> 수정 완료

    @GetMapping("/{qnaId}/update")
    public ApiResponse<String> getQnaToUpdate(Principal principal, @PathVariable Long qnaId, @Valid @RequestBody PostQnaBoard request){
        qnaBoardService.updateQna(principal, qnaId, request);
        return ApiResponse.onSuccess("게시글이 수정되었습니다.");
    }

    /**
     * 문의사항 게시글 수정
     *
     * @param principal
     * @param qnaId
     * @param request
     * @return
     */
    @PostMapping("/{qnaId}/update")
    public ApiResponse<String> qnaUpdate(Principal principal, @PathVariable Long qnaId, @Valid @RequestBody PostQnaBoard request){
        qnaBoardService.updateQna(principal, qnaId, request);

        return ApiResponse.onSuccess("게시글이 수정되었습니다.");
    }
}