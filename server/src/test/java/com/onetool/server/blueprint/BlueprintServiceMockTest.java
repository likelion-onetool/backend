package com.onetool.server.blueprint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.blueprint.controller.BlueprintController;
import com.onetool.server.api.blueprint.controller.SearchController;
import com.onetool.server.api.blueprint.dto.BlueprintSortRequest;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.blueprint.dto.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.api.member.controller.MemberController;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.global.exception.CategoryNotFoundException;
import com.onetool.server.global.exception.codes.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest({BlueprintController.class, MemberController.class, SearchController.class})
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
public class BlueprintServiceMockTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BlueprintService blueprintService;

    @MockBean
    private BlueprintSearchService blueprintSearchService;

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
                "https://onetool.com/download",
                false
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
        Long blueprintId = 1L;

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
                "https://onetool.com/download",
                false
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

    @DisplayName("카테고리 없이 blueprint를 정렬한다.")
    @Test
    void blueprintWithoutCategorySortTest() throws Exception {
        String sortBy = "price";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(0, 10);
        List<BlueprintResponse> mockResponse = List.of(
                BlueprintResponse.builder().id(1L).blueprintName("골프장 1인실 평면도(1)").build(),
                BlueprintResponse.builder().id(2L).blueprintName("골프장 레이아웃 평면도(1)").build()
        );

        Mockito.when(blueprintSearchService.sortBlueprints(
                        new BlueprintSortRequest(null, sortBy, sortOrder), pageable))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/blueprint/sort")
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].blueprintName").value("골프장 1인실 평면도(1)"))
                .andExpect(jsonPath("$.result[1].blueprintName").value("골프장 레이아웃 평면도(1)"));
    }

    @DisplayName("잘못된 카테고리명을 받았을 때 예외가 발생한다.")
    @Test
    void blueprintWithInvalidCategorySortTest() throws Exception {
        String invalidCategoryName = "invalidCategory";
        String sortBy = "price";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(0, 10);

        BlueprintSortRequest request = new BlueprintSortRequest(invalidCategoryName, sortBy, sortOrder);

        // 카테고리별 정렬 메서드 mock
        Mockito.when(blueprintSearchService.sortBlueprints(request, pageable))
                .thenThrow(new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/blueprint/{categoryName}/sort", invalidCategoryName)
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 카테고리입니다."));
    }

    @DisplayName("카테고리 별로 blueprint를 정렬한다.")
    @Test
    void blueprintWithCategorySortTest() throws Exception {
        String categoryName = "building";
        String sortBy = "price";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(0, 10);

        // Mock 응답
        List<BlueprintResponse> mockResponse = List.of(
                BlueprintResponse.builder().id(1L).blueprintName("골프장 1인실 평면도(1)").categoryId(1L).build(),
                BlueprintResponse.builder().id(2L).blueprintName("골프장 레이아웃 평면도(1)").categoryId(1L).build()
        );

        BlueprintSortRequest request = new BlueprintSortRequest(categoryName, sortBy, sortOrder);

        // 카테고리별 정렬 메서드 mock
        Mockito.when(blueprintSearchService.sortBlueprints(request, pageable))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/blueprint/{categoryName}/sort", categoryName)
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].blueprintName").value("골프장 1인실 평면도(1)"))
                .andExpect(jsonPath("$.result[1].blueprintName").value("골프장 레이아웃 평면도(1)"))
                .andExpect(jsonPath("$.result[0].categoryId").value(1L))
                .andExpect(jsonPath("$.result[1].categoryId").value(1L))
                .andExpect(jsonPath("$.result.length()").value(2));
    }

    @Test
    void testGenJwtToken() throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", "admin@example.com",
                "password", "1234"
        );

        MockHttpServletResponse response = mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());
    }
}