package com.onetool.server.cart.repository;

import com.onetool.server.blueprint.Blueprint;
import com.onetool.server.cart.Cart;
import com.onetool.server.cart.CartBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartBlueprintRepository extends JpaRepository<CartBlueprint, Long> {
    boolean existsByCartAndBlueprint(Cart cart, Blueprint blueprint);
}
