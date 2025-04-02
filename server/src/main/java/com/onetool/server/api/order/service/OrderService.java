package com.onetool.server.api.order.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.order.OrderBlueprint;
import com.onetool.server.api.order.Orders;
import com.onetool.server.api.order.repository.OrderJpaRepository;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.order.repository.OrderRepository;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.OrderErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Page<Orders> findAllOrdersByUserId(Long memberId, Pageable pageable) {
        return orderRepository.findByMemberId(memberId, pageable);
    }

    @Transactional
    public Orders findOrdersById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(OrderErrorCode.NOT_FOUND_ERROR,"orderId : "+orderId));
    }

    @Transactional
    public Long saveOrder(Orders orders, Member member, List<Blueprint> blueprintList) {
        validateOrdersIsNull(orders);
        assignAllConnectOrders(orders, member, blueprintList);
        Orders saveOrders = orderRepository.save(orders);

        return saveOrders.getId();
    }

    @Transactional
    public void deleteOrder(Orders orders) {
        validateOrdersIsNull(orders);
        orderRepository.deleteById(orders.getId());
    }

    @Transactional(readOnly = true)
    public List<Orders> findAllByUserId(Long userId) {
        return orderRepository.findByMemberId(userId);
    }

    private void validateOrdersIsNull(Orders orders) {
        if (orders == null) {
            throw new ApiException(OrderErrorCode.NULL_POINT_ERROR,"Orders 객체가 NULL입니다.");
        }
    }

    private void assignAllConnectOrders(Orders orders, Member member, List<Blueprint> blueprintList) {
        orders.assignMember(member);
        blueprintList.forEach(blueprint -> {
            OrderBlueprint orderBlueprint = new OrderBlueprint(blueprint.getDownloadLink());
            orderBlueprint.assignOrder(orders);
            orderBlueprint.assignBlueprint(blueprint);
        });
    }
}