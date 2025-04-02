package com.onetool.server.api.order.controller;

import com.onetool.server.api.order.business.OrderBusiness;
import com.onetool.server.api.order.dto.request.OrderRequest;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderBusiness orderBusiness;

    @PostMapping
    public ApiResponse<Long> orderPost(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Valid @RequestBody OrderRequest request
    ) {
        Long orderId = orderBusiness.createOrder(principal.getContext().getEmail(), request.blueprintIds());
        return ApiResponse.onSuccess(orderId);
    }

    @GetMapping
    public ApiResponse<List<MyPageOrderResponse>> orderGet(
            @AuthenticationPrincipal PrincipalDetails principal,
            Pageable pageable) {
        List<MyPageOrderResponse> myPageOrderResponseList = orderBusiness.getMyPageOrderResponseList(principal.getContext().getId(), pageable);
        return ApiResponse.onSuccess(myPageOrderResponseList);
    }

    @DeleteMapping
    public ApiResponse<Long> orderDelete(
            @RequestBody Long orderId
    ) {
        orderBusiness.removeOrder(orderId);
        return ApiResponse.onSuccess(orderId);
    }
}