package com.onetool.server.api.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.helper.MockBeanInjection;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.fixture.MemberFixture;
import com.onetool.server.api.member.fixture.WithMockPrincipalDetails;
import com.onetool.server.api.order.controller.OrderController;
import com.onetool.server.api.order.dto.request.OrderRequest;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

import static com.onetool.server.api.order.fixture.OrderFixture.createMyPageOrderResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc
public class OrderControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 주문을_요청받고_등록한다() throws Exception {
        // ✅ Given (설정)
        Member member = MemberFixture.createMember();
        OrderRequest orderRequest = new OrderRequest(Set.of(1L, 2L));
        String jsonRequest = objectMapper.writeValueAsString(orderRequest);

        when(orderBusiness.createOrder(anyString(), anySet())).thenReturn(1L);

        // ✅ When (실행)
        ResultActions result = mockMvc
                .perform(post("/orders")
                        .content(jsonRequest)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // ✅ Then (검증)
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 주문목록을_조회한다() throws Exception {
        // ✅ Given (설정)
        Pageable pageable = PageRequest.of(0, 10);
        MyPageOrderResponse myPageOrderResponse1 = createMyPageOrderResponse();
        MyPageOrderResponse myPageOrderResponse2 = createMyPageOrderResponse();
        when(orderBusiness.getMyPageOrderResponseList(anyLong(), eq(pageable))).thenReturn(List.of(myPageOrderResponse1, myPageOrderResponse2));

        // ✅ When (실행)
        ResultActions resultActions = mockMvc.perform(
                get("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
        );

        // ✅ Then (검증)
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.size()").value(2))
                .andExpect(jsonPath("$.result[0].orderId").value(myPageOrderResponse1.orderId()));

        verify(orderBusiness).getMyPageOrderResponseList(eq(2L), eq(pageable));
    }

    @Test
    @WithMockPrincipalDetails(id = 2L)
    void 주문목록을_삭제한다() throws Exception {
        // ✅ Given (설정)
        Long orderId = 1L;
        String jsonBody = objectMapper.writeValueAsString(orderId);
        doNothing().when(orderBusiness).removeOrder(orderId);

        // ✅ When (실행)
        ResultActions result = mockMvc.perform(
                delete("/orders")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // ✅ Then (검증)
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }
}
