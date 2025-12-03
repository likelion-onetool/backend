package com.onetool.server.api.cart.controller;

import com.onetool.server.api.cart.business.CartBusiness;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "장바구니", description = "장바구니 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartBusiness cartBusiness;

    @Operation(summary = "장바구니에 도면 추가 API", description = "특정 도면을 현재 사용자의 장바구니에 추가합니다.")
    @PostMapping("/{blueprintId}")
    public ApiResponse<String> addBlueprintToCart(@AuthenticationPrincipal PrincipalDetails principal,
                                                  @PathVariable Long blueprintId) {
        cartBusiness.addBlueprintToCart(principal.getContext().getId(), blueprintId);
        return ApiResponse.onSuccess("장바구니에 상품이 등록 되었습니다.");
    }

    @Operation(summary = "내 장바구니 조회 API", description = "현재 사용자의 장바구니에 담긴 모든 도면을 조회합니다.")
    @GetMapping
    public ApiResponse<Object> showMyCart(@AuthenticationPrincipal PrincipalDetails principal) {
        return ApiResponse.onSuccess(cartBusiness.getMyCart(principal.getContext().getId()));
    }

    @Operation(summary = "장바구니에서 도면 삭제 API", description = "장바구니에서 특정 도면을 삭제합니다.")
    @DeleteMapping("/{blueprintId}")
    public ApiResponse<String> deleteBlueprintInCart(@AuthenticationPrincipal PrincipalDetails principal,
                                                     @PathVariable Long blueprintId) {
        return ApiResponse.onSuccess(cartBusiness.removeBlueprintInCart(principal.getContext().getId(), blueprintId));
    }
}
