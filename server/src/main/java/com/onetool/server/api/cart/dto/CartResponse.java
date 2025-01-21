package com.onetool.server.api.cart.dto;

import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.cart.CartBlueprint;
import lombok.Builder;

import java.util.List;

public class CartResponse {
    @Builder
    public record CartItems(
            Long totalPrice,
            List<BlueprintResponse> blueprintsInCart
    ){
        public static CartItems cartItems(Long totalPrice, List<CartBlueprint> cartBlueprints){
            return CartItems.builder()
                    .totalPrice(totalPrice)
                    .blueprintsInCart(cartBlueprints.stream()
                            .map(cartItem -> BlueprintResponse.items(cartItem.getBlueprint()))
                            .toList())
                    .build();
        }
    }
}