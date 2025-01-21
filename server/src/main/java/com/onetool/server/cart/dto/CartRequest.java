package com.onetool.server.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CartRequest {
    public record AddBlueprintToCart(
            @NotNull Long blueprintId
    ){}
}