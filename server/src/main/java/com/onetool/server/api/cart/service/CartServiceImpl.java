package com.onetool.server.api.cart.service;

import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.cart.CartBlueprint;
import com.onetool.server.api.cart.repository.CartBlueprintRepository;
import com.onetool.server.api.cart.repository.CartRepository;
import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.onetool.server.api.cart.dto.CartResponse.CartItems;
import static com.onetool.server.global.exception.codes.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final BlueprintRepository blueprintRepository;
    private final CartBlueprintRepository cartBlueprintRepository;

    public Object showCart(MemberAuthContext user){
        Member member = findMemberWithCart(user.getId());
        Cart cart = member.getCart();
        if(cart.isCartEmpty()) return "장바구니에 상품이 없습니다.";
        return CartItems.cartItems(cart.getTotalPrice(), cart.getCartItems());
    }

    public String addBlueprintToCart(MemberAuthContext user,
                                     Long blueprintId){

        Member member = findMemberWithCart(user.getId());
        Cart cart = member.getCart();
        Blueprint blueprint = findBlueprint(blueprintId);
        isBlueprintAlreadyInCart(cart, blueprint);
        CartBlueprint newCartBlueprint = CartBlueprint.addBlueprintToCart(cart, blueprint);
        cartBlueprintRepository.save(CartBlueprint.addBlueprintToCart(cart, blueprint));
        updateCartAndSave(cart, newCartBlueprint, Action.ADD);
        return "장바구니에 추가됐습니다.";
    }

    public String deleteBlueprintInCart(MemberAuthContext user,
                                      Long blueprintId){

        Member member = findMemberWithCart(user.getId());
        Cart cart = member.getCart();
        CartBlueprint cartBlueprint = findCartBlueprint(cart, blueprintId);
        cartBlueprintRepository.delete(cartBlueprint);
        updateCartAndSave(cart, cartBlueprint, Action.REMOVE);
        return "삭제됐습니다.";
    }

    private Blueprint findBlueprint(Long blueprintId) {
        return blueprintRepository.findById(blueprintId).orElseThrow(() -> new BaseException(NO_BLUEPRINT_FOUND));
    }

    private Member findMemberWithCart(Long id) {
        return memberRepository
                .findByIdWithCart(id)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
    }

    private void isBlueprintAlreadyInCart(Cart cart, Blueprint blueprint) {
        if(cartBlueprintRepository.existsByCartAndBlueprint(cart, blueprint)) throw new BaseException(ALREADY_EXIST_BLUEPRINT_IN_CART);
    }

    private CartBlueprint findCartBlueprint(Cart cart, Long blueprintId) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBlueprint().getId().equals(blueprintId))
                .findFirst()
                .orElseThrow(() -> new BaseException(NO_BLUEPRINT_FOUND));
    }

    private void updateCartAndSave(Cart cart, CartBlueprint cartBlueprint, Action action) {
        if (action == Action.ADD) {
            cart.getCartItems().add(cartBlueprint);
        } else if (action == Action.REMOVE) {
            cart.getCartItems().remove(cartBlueprint);
        }

        long totalPrice = calculateTotalPrice(cart);
        cart.updateTotalPrice(totalPrice);
        cartRepository.save(cart);
    }

    private long calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToLong(item -> item.getBlueprint().getStandardPrice())
                .sum();
    }

    private enum Action {
        ADD,
        REMOVE
    }

}