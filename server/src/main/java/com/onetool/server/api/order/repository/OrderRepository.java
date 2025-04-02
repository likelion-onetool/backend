package com.onetool.server.api.order.repository;

import com.onetool.server.api.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface OrderRepository {

    Optional<Orders> findById(Long id);
    Orders save(Orders orders);
    void deleteById(Long id);

    List<Orders> findByMemberId( Long memberId);
    Page<Orders> findByMemberId(Long memberId, Pageable pageable);
}
