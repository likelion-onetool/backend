package com.onetool.server.api.cart;

import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.api.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_cart_id")
    private Member member;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    private List<CartBlueprint> cartItems = new ArrayList<>();

    private Cart(Member member) {
        this.member = member;
        this.totalPrice = 0L;
    }

    public static Cart createCart(Member member) {
        return new Cart(member);
    }

    public void updateTotalPrice() {
        this.totalPrice = this.getCartItems()
                .stream()
                .mapToLong(item -> item.getBlueprint().getStandardPrice())
                .sum();
    }

    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
}