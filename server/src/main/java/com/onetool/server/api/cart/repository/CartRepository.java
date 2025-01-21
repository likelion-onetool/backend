package com.onetool.server.api.cart.repository;

import com.onetool.server.api.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
