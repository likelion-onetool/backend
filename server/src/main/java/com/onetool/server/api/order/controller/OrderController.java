package com.onetool.server.api.order.controller;

import com.onetool.server.api.order.business.OrderBusiness;
import com.onetool.server.api.order.dto.request.OrderRequest;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "주문", description = "주문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderBusiness orderBusiness;

    @Operation(summary = "주문 생성 API", description = "새로운 주문을 생성합니다.")
    @PostMapping
    public ApiResponse<Long> ordersPost(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Valid @RequestBody OrderRequest request
    ) {
        Long orderId = orderBusiness.createOrder(principal, request);
        return ApiResponse.onSuccess(orderId);
    }

    @Operation(summary = "내 주문 목록 조회 API", description = "현재 로그인된 사용자의 주문 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<MyPageOrderResponse>> ordersGet(
            @AuthenticationPrincipal PrincipalDetails principal,
            Pageable pageable) {
        List<MyPageOrderResponse> myPageOrderResponseList = orderBusiness.getMyPageOrderResponseList(principal, pageable);
        return ApiResponse.onSuccess(myPageOrderResponseList);
    }

    @Operation(summary = "주문 취소(삭제) API", description = "특정 주문을 취소(삭제)합니다.")
    @DeleteMapping
    public ApiResponse<Long> ordersDelete(
            @RequestBody Long orderId
    ) {
        orderBusiness.removeOrders(orderId);
        return ApiResponse.onSuccess(orderId);
    }
}
