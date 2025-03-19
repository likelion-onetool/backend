package com.onetool.server.api.order.dto.response;

import com.onetool.server.api.order.Orders;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Slf4j
public record MyPageOrderResponse(
        String orderName,
        Long orderId,
        Long totalPrice,
        String status,
        List<String> downloadUrl
){
    public static List<MyPageOrderResponse> from(List<Orders> ordersList){
        List<MyPageOrderResponse> dtos = new ArrayList<>();
        if(isOrderEmpty(ordersList)) {
            return dtos;
        }

        ordersList.forEach(orders -> {
            dtos.add(
                    MyPageOrderResponse.builder()
                            .orderId(orders.getId())
                            .orderName(createOrderName(orders))
                            .totalPrice(orders.getTotalPrice())
                            .downloadUrl(orders.getOrderItemsDownloadLinks())
                            .status(orders.getStatus().name())
                            .build()
            );
        });
        return dtos;
    }

    private static String createOrderName(Orders orders) {
        log.info("order_id : {}",orders.getId());
        StringBuilder sb = new StringBuilder();
        String firstBlueprintName = orders.getOrderItems().get(0).getBlueprint().getBlueprintName();
        int blueprintCount = orders.getOrderItems().size();
        sb.append(firstBlueprintName);

        if (blueprintCount > 1) {
            sb.append("외 ");
            sb.append(blueprintCount - 1);
            sb.append("개");
        }

        return sb.toString();
    }

    private static boolean isOrderEmpty(List<Orders> ordersList) {
        return ordersList.isEmpty();
    }
}