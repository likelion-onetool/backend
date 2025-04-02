package com.onetool.server.api.order.repository;

import com.onetool.server.api.order.Order;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@Primary
public interface OrderJpaRepository extends OrderRepository, Repository<Order, Long> {


    Optional<Order> findById(Long id);

    Order save(Order order);

    void deleteById(Long id);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.payment WHERE o.member.id = :memberId")
    List<Order> findByMemberId(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = {"payment"})
    @Query("SELECT o FROM Order o WHERE o.member.id = :memberId")
    Page<Order> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);


}