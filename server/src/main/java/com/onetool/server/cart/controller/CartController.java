package com.onetool.server.cart.controller;

import com.onetool.server.blueprint.dto.BlueprintResponse;
import com.onetool.server.cart.service.CartService;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 도면 상세페이지에서 장바구니에 추가
     * @param principal
     * @param blueprintId
     * @return
     */
    @PostMapping("/api/cart/add/{blueprintId}")
    public ApiResponse<String> addBlueprintToCart(@AuthenticationPrincipal PrincipalDetails principal,
                                             @PathVariable Long blueprintId){
        return ApiResponse.onSuccess(cartService.addBlueprintToCart(principal.getContext(), blueprintId));
    }

    @GetMapping("/cart")
    public ApiResponse<Object> showMyCart(@AuthenticationPrincipal PrincipalDetails principal){
        return ApiResponse.onSuccess(cartService.showCart(principal.getContext()));
    }

    @DeleteMapping("/api/cart/delete/{blueprintId}")
    public ApiResponse<String> deleteBlueprintInCart(@AuthenticationPrincipal PrincipalDetails principal,
                                                @PathVariable Long blueprintId){
        return ApiResponse.onSuccess(cartService.deleteBlueprintInCart(principal.getContext(), blueprintId));
    }
}
