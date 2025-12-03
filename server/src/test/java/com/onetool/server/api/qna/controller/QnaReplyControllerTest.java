package com.onetool.server.api.qna.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.helper.MockBeanInjection;
import com.onetool.server.api.member.fixture.WithMockPrincipalDetails;
import com.onetool.server.api.qna.business.QnaReplyBusiness;
import com.onetool.server.api.qna.controller.QnaReplyController;
import com.onetool.server.api.qna.dto.request.ModifyQnaReplyRequest;
import com.onetool.server.api.qna.dto.request.PostQnaReplyRequest;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QnaReplyController.class)
@AutoConfigureMockMvc
class QnaReplyControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 응답을_추가한다() throws Exception {
        // ✅ Given (설정)
        Long qnaId = 1L;
        PostQnaReplyRequest request = mock(PostQnaReplyRequest.class);

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                post("/qna/{qnaId}/reply", qnaId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("댓글이 등록됐습니다."));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 응답을_삭제합니다() throws Exception {
        // ✅ Given (설정)
        Long qnaId = 1L;
        Long replyId = 1L;

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                delete("/qna/{qnaId}/reply/{replyId}", qnaId, replyId)
                        .with(csrf())
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("댓글이 삭제됐습니다."));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 응답을_갱신한다() throws Exception{
        // ✅ Given (설정)
        Long qnaId = 1L;
        Long replyId = 1L;
        ModifyQnaReplyRequest request = new ModifyQnaReplyRequest(replyId, "수정된 댓글 내용");

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                patch("/qna/{qnaId}/reply/{replyId}", qnaId, replyId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("댓글이 수정됐습니다."));
    }
}