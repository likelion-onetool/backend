package com.onetool.server.api.cart.repository;

import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.cart.CartBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN FETCH c.member m WHERE m.id = :memberId")
    Optional<Cart> findCartIdWithMemberByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT DISTINCT ci FROM CartBlueprint ci WHERE ci.cart.id = :cartId")
    List<CartBlueprint> findCartBlueprintsByCartId(@Param("cartId") Long cartId);

    @Query("SELECT c.totalPrice FROM Cart c WHERE c.id = :cartId")
    Long findTotalPriceByCartId(@Param("cartId") Long cartId);
}