package com.onetool.server.cart.service;

import com.onetool.server.blueprint.Blueprint;
import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.member.domain.Member;


public interface CartService {
    Object showCart(MemberAuthContext user);

    String addBlueprintToCart(MemberAuthContext user, Long blueprintId);

    String deleteBlueprintInCart(MemberAuthContext user, Long blueprintId);
}