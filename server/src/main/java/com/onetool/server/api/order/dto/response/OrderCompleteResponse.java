package com.onetool.server.api.order.dto.response;

import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.order.Order;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderCompleteResponse(
        Long totalPrice,
        List<BlueprintResponse> blueprints
){
    public static OrderCompleteResponse response(Order order){
        return OrderCompleteResponse.builder()
                .totalPrice(order.getTotalPrice())
                .blueprints(order.getOrderItems()
                        .stream()
                        .map(orderBlueprint ->
                                BlueprintResponse.items(orderBlueprint.getBlueprint()))
                        .toList())
                .build();
    }
}