package com.onetool.server.api.cart.repository;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.cart.CartBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartBlueprintRepository extends JpaRepository<CartBlueprint, Long> {
    boolean existsByCartAndBlueprint(Cart cart, Blueprint blueprint);
}