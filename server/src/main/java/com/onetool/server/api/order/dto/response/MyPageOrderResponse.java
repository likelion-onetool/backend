package com.onetool.server.api.order.dto.response;

import com.onetool.server.api.order.Order;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder
@Slf4j
public record MyPageOrderResponse(
        String orderName,
        Long orderId,
        Long totalPrice,
        String status,
        List<String> downloadUrl
){
    public static List<MyPageOrderResponse> from(List<Order> orderList){
        List<MyPageOrderResponse> dtos = new ArrayList<>();
        if(isOrderEmpty(orderList)) {
            return dtos;
        }

        orderList.forEach(orders -> {
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

    private static String createOrderName(Order order) {
        log.info("order_id : {}", order.getId());
        StringBuilder sb = new StringBuilder();
        String firstBlueprintName = order.getOrderItems().get(0).getBlueprint().getBlueprintName();
        int blueprintCount = order.getOrderItems().size();
        sb.append(firstBlueprintName);

        if (blueprintCount > 1) {
            sb.append("외 ");
            sb.append(blueprintCount - 1);
            sb.append("개");
        }

        return sb.toString();
    }

    private static boolean isOrderEmpty(List<Order> orderList) {
        return orderList.isEmpty();
    }
}