package com.onetool.server.api.order.fixture;

import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.api.payments.domain.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderFixture {

    public static Order createOrder(){
        return Order.builder()
                .totalCount(1)
                .totalPrice(1000L)
                .status(PaymentStatus.PAY_DONE)
                .build();
    }

    public static Order createOrder(Long id){
        return Order.builder()
                .id(id)
                .totalCount(1)
                .totalPrice(1000L)
                .status(PaymentStatus.PAY_DONE)
                .orderItems(new ArrayList<>())
                .build();
    }

    public static MyPageOrderResponse createMyPageOrderResponse(){
        return new MyPageOrderResponse("테스트주문",1L,1000L,"테스트상태",List.of("testuri"));

    }
}
