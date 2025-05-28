package com.onetool.server.api.cart.controller;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.cart.business.CartBusiness;
import com.onetool.server.api.cart.dto.response.CartItemsResponse;
import com.onetool.server.api.cart.fixture.CartFixture;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.fixture.MemberFixture;
import com.onetool.server.api.member.fixture.WithMockPrincipalDetails;
import com.onetool.server.global.auth.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartBusiness cartBusiness;

    @MockBean
    private JwtUtil jwtUtil;

    private Member member;
    private Cart cart;

    private final String cartEndpointPrefix = "/api/cart";

    @BeforeEach
    void setUp(){
        member = MemberFixture.createMember();
        cart = Cart.createCart(member);
        member.update(cart);
    }

    @Test
    @WithMockPrincipalDetails
    void 장바구니_도면_추가_성공() throws Exception {
        //given
        long blueprintId = 1L;

        //when & then
        mockMvc.perform(post(cartEndpointPrefix + "/{blueprintId}", blueprintId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("장바구니에 상품이 등록 되었습니다."));
    }

    @Test
    @WithMockPrincipalDetails
    void 장바구니_조회_성공() throws Exception {
        //given
        List<Blueprint> blueprints = CartFixture.createBlueprints();
        Cart cart = member.getCart();
        CartFixture.createCartBlueprints(cart, blueprints);
        CartItemsResponse response = CartItemsResponse.from(cart);
        given(cartBusiness.getMyCart(anyLong())).willReturn(response);


        //when & then
        mockMvc.perform(get(cartEndpointPrefix)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalPrice").value(cart.getTotalPrice()))
                .andExpect(jsonPath("$.result.blueprintsInCart[0].blueprintName")
                        .value(response.blueprintsInCart().get(0).blueprintName()))
                .andExpect(jsonPath("$.result.blueprintsInCart[1].blueprintName")
                        .value(response.blueprintsInCart().get(1).blueprintName()))
                .andExpect(jsonPath("$.result.blueprintsInCart[2].blueprintName")
                        .value(response.blueprintsInCart().get(2).blueprintName()));
    }

    @Test
    @WithMockPrincipalDetails
    void 장바구니_비어있음_조회_성공() throws Exception {
        //given
        String cartEmpty = "장바구니가 비어있습니다.";
        given(cartBusiness.getMyCart(anyLong())).willReturn(cartEmpty);

        //when & then
        mockMvc.perform(get(cartEndpointPrefix)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("장바구니가 비어있습니다."));

    }

    @Test
    @WithMockPrincipalDetails
    void 장바구니_도면_삭제_성공() throws Exception {
        //given
        String deleteMessage = "삭제되었습니다";
        given(cartBusiness.removeBlueprintInCart(anyLong(), anyLong())).willReturn(deleteMessage);

        //when & then
        mockMvc.perform(delete(cartEndpointPrefix + "/{blueprintId}", 1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("삭제되었습니다"));
    }
}
