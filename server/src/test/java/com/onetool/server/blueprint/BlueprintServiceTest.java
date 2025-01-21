package com.onetool.server.blueprint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.blueprint.controller.BlueprintController;
import com.onetool.server.api.blueprint.dto.SearchResponse;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.blueprint.dto.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.api.member.controller.MemberController;
import com.onetool.server.api.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({BlueprintController.class, MemberController.class})
@ActiveProfiles("test")
public class BlueprintServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BlueprintService blueprintService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        MemberAuthContext authContext = new MemberAuthContext(1L, "admin", "ROLE_ADMIN", "admin@example.com", "1234");
        PrincipalDetails principalDetails = new PrincipalDetails(authContext);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication = new TestingAuthenticationToken(principalDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String obtainAccessToken(String email, String password) throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", email,
                "password", password
        );

        MockHttpServletResponse response = mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        ApiResponse<Map<String, String>> apiResponse = objectMapper.readValue(
                response.getContentAsString(), ApiResponse.class);

        return apiResponse.getResult().get("accessToken");
    }

    @DisplayName("키워드 기반 검색이 잘되는지 확인")
    @Test
    void search_with_keyword() {
        String keyword = "마을";
        Pageable pageable = PageRequest.of(0, 5);
        Page<SearchResponse> response = blueprintService.searchNameAndCreatorWithKeyword(keyword, pageable);
        assertThat(response.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("새 blueprint가 정상적으로 생성되는지 확인")
    @Test
    public void 새로운_blueprint를_생성한다() throws Exception {
        // Given
        BlueprintRequest blueprintRequest = new BlueprintRequest(
                1L,
                "대한민국 마을",
                1L,
                50000L,
                "https://s3.bucket.image.com/",
                "대한민국의 어느 마을의 청사진입니다.",
                ".exe",
                "CAD",
                BigInteger.valueOf(100),
                40000L,
                LocalDateTime.now().plusDays(10),
                "윤성원 작가",
                "https://onetool.com/download"
        );

        // When
        String accessToken = obtainAccessToken("admin@example.com", "1234");

        MockHttpServletResponse response = mockMvc.perform(post("/blueprint/upload")
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blueprintRequest)))
                .andExpect(status().isOk()) // 기대하는 응답 상태
                .andReturn()
                .getResponse();

        // Then
        ApiResponse<String> apiResponse = objectMapper.readValue(response.getContentAsString(), ApiResponse.class);
        assertThat(apiResponse.getResult()).isEqualTo("상품이 정상적으로 등록되었습니다.");
    }


    @DisplayName("blueprint id를 통해 상세 정보 조회가 되는지 확인")
    @Test
    public void blueprint_id로_상세정보를_조회한다() throws Exception {
        // given
        Long blueprintId = 1354L;

        // when&then
        mockMvc.perform(get("/blueprint/" + blueprintId)
                        .with(csrf())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess").value(true));

    }

    @DisplayName("blueprint가 정상적으로 업데이트되는지 확인")
    @Test
    public void blueprint를_업데이트한다() throws Exception {
        // given
        BlueprintResponse blueprintResponse = new BlueprintResponse(
                3L,
                "대한민국 마을",
                1L,
                50000L,
                "https://s3.bucket.image.com/",
                "한국의 어느 마을의 청사진입니다.",
                ".exe",
                "CAD",
                BigInteger.valueOf(100),
                40000L,
                LocalDateTime.now().plusDays(10),
                "윤성원 작가",
                "https://onetool.com/download"
        );

        String accessToken = obtainAccessToken("admin@example.com", "1234");

        // when & then
        mockMvc.perform(put("/blueprint/update")
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(blueprintResponse)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(ApiResponse.onSuccess("상품이 정상적으로 수정 되었습니다."))));
    }

    @DisplayName("blueprint가 정상적으로 삭제되는지 확인")
    @Test
    public void blueprint를_삭제한다() throws Exception {
        // given
        Long blueprintId = 3L;
        String accessToken = obtainAccessToken("admin@example.com", "1234");

        // when&then
        mockMvc.perform(delete("/blueprint/delete/" + blueprintId)
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(ApiResponse.onSuccess("상품이 정상적으로 삭제 되었습니다."))));
    }
}