package com.onetool.server.api.qna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.helper.MockBeanInjection;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.fixture.WithMockPrincipalDetails;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.api.qna.dto.request.PostQnaBoardRequest;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.api.qna.dto.response.QnaBoardDetailResponse;
import com.onetool.server.api.fixture.MemberFixture;
import com.onetool.server.global.auth.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.onetool.server.api.fixture.QnaBoardFixture.createQnaBoard;
import static com.onetool.server.api.fixture.QnaBoardFixture.createQnaBoardDetailResponse;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QnaBoardController.class)
@AutoConfigureMockMvc
class QnaBoardControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockPrincipalDetails(id = 2L)
        // 상세 과정 질문하기
    void 질문_리스트를_조회한다() throws Exception {
        // ✅ Given (설정)
        Member member = MemberFixture.createMember(2L);
        QnaBoard qnaBoard1 = createQnaBoard(1L, member);
        QnaBoard qnaBoard2 = createQnaBoard(2L, member);
        List<QnaBoardBriefResponse> response = QnaBoardBriefResponse.fromQnaBoardListToBriefResponseList(List.of(qnaBoard1, qnaBoard2));

        when(qnaBoardBusiness.getQnaBoardBriefList()).thenReturn(response);
        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                get("/qna/list")
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].title").value("Title1"))
                .andExpect(jsonPath("$.result[0].writer").value("User2"))
                .andExpect(jsonPath("$.result[1].title").value("Title2"))
                .andExpect(jsonPath("$.result[1].writer").value("User2"));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 질문을_등록한다() throws Exception {
        // ✅ Given (설정)
        PostQnaBoardRequest request = new PostQnaBoardRequest("테스트제목", "테스트내용", "테스트작가");

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                post("/qna/post")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("문의사항 등록이 완료됐습니다."));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 특정_질문을_조회한다() throws Exception {
        // ✅ Given (설정)
        Long qnaId = 1L;
        QnaBoardDetailResponse response = createQnaBoardDetailResponse();
        when(qnaBoardBusiness.getQnaBoardDetail("user2@example.com", 1L)).thenReturn(response);

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                get("/qna/{qnaId}", qnaId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("테스트제목"));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 질문_삭제() throws Exception {
        // ✅ Given (설정)
        Long qnaId = 1L;

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                delete("/qna/{qnaId}", qnaId)
                        .with(csrf())
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("게시글이 삭제되었습니다."));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 질문을_갱신한다() throws Exception {
        // ✅ Given (설정)
        Long qnaId = 2L;
        PostQnaBoardRequest request = new PostQnaBoardRequest("수정된 제목", "수정된 내용", "수정된 작성자");

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                patch("/qna/{qnaId}", qnaId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("게시글이 수정되었습니다."));
    }
}