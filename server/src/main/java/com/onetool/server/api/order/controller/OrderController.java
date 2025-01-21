package com.onetool.server.api.order.controller;

import com.onetool.server.api.order.service.OrderServiceImpl;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.api.order.dto.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.onetool.server.api.order.dto.response.OrderResponse.*;


@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    //결제 페이지로 이동
    @GetMapping("/orders")
    public ApiResponse<OrderPageMemberResponseDto> getUserInformationForOrder(@AuthenticationPrincipal PrincipalDetails principal,
                                                                              @RequestBody OrderRequest request){
        return ApiResponse.onSuccess(orderService.getMemberInfoForOrder(principal.getContext(), request));
    }

}