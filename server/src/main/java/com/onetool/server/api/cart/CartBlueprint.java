package com.onetool.server.api.cart;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartBlueprint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    //도면 상품은 도면 엔티티가 나오는 대로 짤게요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;

    @Builder
    private CartBlueprint(Cart cart, Blueprint blueprint){
        this.cart = cart;
        this.blueprint = blueprint;
    }

    public static CartBlueprint of(Cart cart, Blueprint blueprint){
        return CartBlueprint.builder()
                .cart(cart)
                .blueprint(blueprint)
                .build();
    }

}