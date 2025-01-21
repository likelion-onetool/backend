package com.onetool.server.order.repository;

import com.onetool.server.order.OrderBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDiabetesRepository extends JpaRepository<OrderBlueprint, Long> {
}
