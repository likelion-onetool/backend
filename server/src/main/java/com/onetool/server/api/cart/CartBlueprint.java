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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    //도면 상품은 도면 엔티티가 나오는 대로 짤게요
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;

    @Builder
    public CartBlueprint(Cart cart, Blueprint blueprint) {
        this.cart = cart;
        this.blueprint = blueprint;
    }

    public static CartBlueprint create(Cart cart, Blueprint blueprint) {
        CartBlueprint cartBlueprint = new CartBlueprint();
        cartBlueprint.assignCart(cart);
        cartBlueprint.assignBlueprint(blueprint);
        return cartBlueprint;
    }

    //연관관계 맺기~~~~~~~~~
    public void assignCart(Cart cart) {
        this.cart = cart;
        cart.getCartItems().add(this);
    }

    public void assignBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
        blueprint.getCartBlueprints().add(this);
    }

    public void deleteCartBlueprint(){
        cart.getCartItems().remove(this);
        this.cart = null;
        blueprint.getCartBlueprints().remove(this);
        this.blueprint = null;
    }
}