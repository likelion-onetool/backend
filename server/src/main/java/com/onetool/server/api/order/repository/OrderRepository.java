package com.onetool.server.api.order.repository;

import com.onetool.server.api.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface OrderRepository {

    Optional<Order> findById(Long id);
    Order save(Order order);
    void deleteById(Long id);

    List<Order> findByMemberId(Long memberId);
    Page<Order> findByMemberId(Long memberId, Pageable pageable);
}
