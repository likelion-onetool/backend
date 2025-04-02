package com.onetool.server.api.order.repository;

import com.onetool.server.api.order.OrderBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface OrderBlueprintJpaRepository extends OrderBlueprintRepository, Repository<OrderBlueprint, Long> {
}