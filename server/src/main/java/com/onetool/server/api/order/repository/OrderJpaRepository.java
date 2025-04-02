package com.onetool.server.api.order.repository;

import com.onetool.server.api.order.Orders;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@Primary
public interface OrderJpaRepository extends OrderRepository, Repository<Orders, Long> {


    Optional<Orders> findById(Long id);

    Orders save(Orders orders);

    void deleteById(Long id);

    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.payment WHERE o.member.id = :memberId")
    List<Orders> findByMemberId(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = {"payment"})
    @Query("SELECT o FROM Orders o WHERE o.member.id = :memberId")
    Page<Orders> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);


}