package com.onetool.server.api.order.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.order.Orders;
import com.onetool.server.api.order.dto.request.OrderRequest;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.api.order.service.OrderService;
import com.onetool.server.global.annotation.Business;
import com.onetool.server.global.auth.login.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
@Slf4j
public class OrderBusiness {

    private final OrderService orderService;
    private final MemberService memberService;
    private final BlueprintService blueprintService;

    @Transactional
    public Long createOrder(PrincipalDetails principalDetails, OrderRequest orderRequest) {
        Member member = memberService.findOne(principalDetails.getContext().getEmail());
        List<Blueprint> blueprintList = blueprintService.findAllBlueprintByIds(orderRequest.blueprintIds());
        Orders orders = new Orders(blueprintList);

        return orderService.saveOrder(orders, member, blueprintList);
    }

    @Transactional
    public List<MyPageOrderResponse> getMyPageOrderResponseList(@AuthenticationPrincipal PrincipalDetails principal) {
        Member member = memberService.findOneWithCart(principal.getContext().getId());
        List<Orders> ordersList = orderService.findAllOrdersByUserId(member.getId());
        List<Blueprint> blueprintList = blueprintService.findAll();
        return MyPageOrderResponse.from(ordersList);
    }

    @Transactional
    public void removeOrders(Long orderId) {
        Orders orders = orderService.findOrdersById(orderId);
        orderService.deleteOrder(orders);
    }
}
