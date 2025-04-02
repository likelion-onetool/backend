package com.onetool.server.api.order.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.order.OrderBlueprint;
import com.onetool.server.api.order.Order;
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
    public Page<Order> findAllOrderByUserId(Long memberId, Pageable pageable) {
        return orderRepository.findByMemberId(memberId, pageable);
    }

    @Transactional
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(OrderErrorCode.NOT_FOUND_ERROR,"orderId : "+orderId));
    }

    @Transactional
    public Long saveOrder(Order order, Member member, List<Blueprint> blueprintList) {
        validateOrderIsNull(order);
        assignAllConnectOrders(order, member, blueprintList);
        Order saveOrder = orderRepository.save(order);

        return saveOrder.getId();
    }

    @Transactional
    public void deleteOrder(Order order) {
        validateOrderIsNull(order);
        orderRepository.deleteById(order.getId());
    }

    @Transactional(readOnly = true)
    public List<Order> findAllByUserId(Long userId) {
        return orderRepository.findByMemberId(userId);
    }

    private void validateOrderIsNull(Order order) {
        if (order == null) {
            throw new ApiException(OrderErrorCode.NULL_POINT_ERROR,"Orders 객체가 NULL입니다.");
        }
    }

    private void assignAllConnectOrders(Order order, Member member, List<Blueprint> blueprintList) {
        order.assignMember(member);
        blueprintList.forEach(blueprint -> {
            OrderBlueprint orderBlueprint = new OrderBlueprint(blueprint.getDownloadLink());
            orderBlueprint.assignOrder(order);
            orderBlueprint.assignBlueprint(blueprint);
        });
    }
}