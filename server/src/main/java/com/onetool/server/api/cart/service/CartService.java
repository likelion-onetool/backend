package com.onetool.server.api.cart.service;

import com.onetool.server.global.auth.MemberAuthContext;


public interface CartService {
    Object showCart(MemberAuthContext user);

    String addBlueprintToCart(MemberAuthContext user, Long blueprintId);

    String deleteBlueprintInCart(MemberAuthContext user, Long blueprintId);
}