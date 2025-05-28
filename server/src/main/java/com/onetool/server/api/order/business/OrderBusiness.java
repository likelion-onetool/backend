package com.onetool.server.api.order.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.api.order.service.OrderService;
import com.onetool.server.global.annotation.Business;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@Business
@RequiredArgsConstructor
@Slf4j
public class OrderBusiness {

    private final OrderService orderService;
    private final MemberService memberService;
    private final BlueprintService blueprintService;

    @Transactional
    public Long createOrder(String userEmail, Set<Long> blueprintIds) {
        Member member = memberService.findOne(userEmail);
        List<Blueprint> blueprintList = blueprintService.findAllBlueprintByIds(blueprintIds);
        Order order = new Order(blueprintList);

        return orderService.saveOrder(order, member, blueprintList);
    }

    @Transactional
    public List<MyPageOrderResponse> getMyPageOrderResponseList(Long userId, Pageable pageable) {
        Member member = memberService.findOneWithCart(userId);
        Page<Order> ordersList = orderService.findAll(member.getId(), pageable);
        List<Blueprint> blueprintList = blueprintService.findAll();
        return MyPageOrderResponse.from(ordersList.getContent());
    }

    @Transactional
    public void removeOrder(Long orderId) {
        Order order = orderService.findOne(orderId);
        orderService.deleteOrder(order);
    }
}
