package com.onetool.server.api.cart.controller;

import com.onetool.server.api.cart.dto.response.CartSessionResponse;
import com.onetool.server.api.cart.service.CartSessionService;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.codes.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "장바구니 (세션)", description = "비회원 장바구니 관련 API")
@Slf4j
@AllArgsConstructor
@RestController
public class CartSessionController {

    private final CartSessionService cartSessionService;

    @Operation(summary = "세션에 장바구니 아이템 추가 API", description = "비회원 장바구니의 세션에 아이템을 추가합니다.")
    @PostMapping("cart/session/add")
    public ApiResponse<?> addCartItemsFromSession(HttpServletRequest httpServletRequest, @RequestBody List<Long> cartItemIds) {
        HttpSession session = httpServletRequest.getSession(false);
        session.setAttribute("cartItemIds", cartItemIds);
        return ApiResponse.onSuccess("세션에 아이템(들)이 추가되었습니다.");
    }

    @Operation(summary = "세션에서 장바구니 아이템 제거 API", description = "비회원 장바구니의 세션에서 특정 아이템을 제거합니다.")
    @PatchMapping("/cart/session/remove")
    public ApiResponse<?> removeCartItemsFromSession(HttpServletRequest httpServletRequest, @RequestBody Long cartItemId) {
        HttpSession session = httpServletRequest.getSession();
        List<Long> items = (List<Long>) session.getAttribute("cartItemIds");
        if (items != null) {
            items.remove(cartItemId);
            session.setAttribute("itemList", items);
            return ApiResponse.onSuccess("해당 아이템이 세션에서 제거되었습니다.");
        } else {
            return ApiResponse.onFailure(ErrorCode.BAD_REQUEST.name(), "세션에 존재하지 않는 아이템입니다.", null);
        }
    }

    @Operation(summary = "세션의 장바구니 아이템 조회 API", description = "비회원 장바구니의 세션에 있는 모든 아이템을 조회합니다.")
    @GetMapping("/cart/session/get")
    public ApiResponse<?> getCartItemsFromSession(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        List<Long> items = (List<Long>) session.getAttribute("cartItemIds");
        List<CartSessionResponse> responses = cartSessionService.getCartSessions(items);
        if (responses != null) {
            return ApiResponse.onSuccess(responses);
        } else {
            return ApiResponse.onFailure(ErrorCode.BAD_REQUEST.name(), "세션에 아이템이 존재하지 않습니다.", null);
        }
    }

    @Operation(summary = "세션의 장바구니 비우기 API", description = "비회원 장바구니의 세션을 비웁니다.")
    @DeleteMapping("/cart/session/drop")
    public ApiResponse<?> dropCartItemsFromSession(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        session.invalidate();
        return ApiResponse.onSuccess("세션 제거가 완료되었습니다.");
    }
}
