package com.onetool.server.api.order.repository;

import com.onetool.server.api.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}