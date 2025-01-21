package com.onetool.server.cart;

import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_cart_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartBlueprint> cartItems = new ArrayList<>();

    private Cart(Member member){
        this.member = member;
        this.totalPrice = 0L;
    }

    public static Cart createCart(Member member){
        return new Cart(member);
    }

    public void updateTotalPrice(Long totalPrice){
        this.totalPrice = totalPrice;
    }

    public boolean isCartEmpty(){
        return cartItems.isEmpty();
    }
}