package com.onetool.server.order.dto.request;

import java.util.List;

public record OrderRequest(
        Long totalPrice,
        List<OrderItem> orderList
) {}