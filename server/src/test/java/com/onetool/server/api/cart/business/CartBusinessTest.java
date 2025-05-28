package com.onetool.server.api.cart.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.cart.CartBlueprint;
import com.onetool.server.api.cart.dto.response.CartItemResponse;
import com.onetool.server.api.cart.dto.response.CartItemsResponse;
import com.onetool.server.api.cart.fixture.CartFixture;
import com.onetool.server.api.cart.service.CartService;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class CartBusinessTest {
    @Mock
    private CartService cartService;

    @Mock
    private BlueprintService blueprintService;

    @InjectMocks
    private CartBusiness cartBusiness;

    private Member member;
    private Blueprint blueprint;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        member = MemberFixture.createMember();
        cart = Cart.createCart(member);
        member.update(cart);

        blueprint = CartFixture.createBlueprint();
    }

    @Test
    void 장바구니에_도면_추가_성공(){
        //given
        given(cartService.findCartById(anyLong())).willReturn(cart);
        given(blueprintService.findBlueprintById(anyLong())).willReturn(blueprint);

        //when
        cartBusiness.addBlueprintToCart(member.getId(), blueprint.getId());

        //then
        verify(cartService).saveCart(any(Cart.class), any(Blueprint.class));
    }

    @Test
    void 장바구니_조회_성공(){
        //given
        given(cartService.findCartById(anyLong())).willReturn(cart);

        //when
        CartBlueprint.create(cart, blueprint);
        CartItemsResponse result = (CartItemsResponse) cartBusiness.getMyCart(member.getId());

        //then
        assertThat(result.totalPrice())
                .isEqualTo(cart.getTotalPrice());

        assertThat(result.blueprintsInCart())
                .usingRecursiveComparison()
                .isEqualTo(List.of(CartItemResponse.from(blueprint)));
    }

    @Test
    void 장바구니_비어있음_성공(){
        //given
        given(cartService.findCartById(anyLong())).willReturn(cart);

        //when & then
        assertThat((String) cartBusiness.getMyCart(member.getId()))
                .isEqualTo("장바구니가 비었습니다.");
    }

    @Test
    void 장바구니_도면_삭제_성공(){
        //given
        given(cartService.findCartById(anyLong())).willReturn(cart);
        given(cartService.findCartBlueprint(any(Cart.class), anyLong()))
                .willReturn(CartBlueprint.builder().build());
        doNothing().when(cartService).deleteCartBlueprint(any(Cart.class), any(CartBlueprint.class));
        // when & then
        assertThat(cartBusiness.removeBlueprintInCart(member.getId(), blueprint.getId()))
                .isEqualTo("삭제되었습니다");
    }
}
