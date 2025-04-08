package com.onetool.server.Util;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.OrderBlueprint;
import org.springframework.expression.spel.ast.Assign;

import static com.onetool.server.api.order.fixture.OrderBlueprintFixture.createOrderBlueprint;

public class AssignUtil {

    public static OrderBlueprint createAndAssignOrderBlueprint(Long orderBlueprintId, Order order, Blueprint blueprint) {
        OrderBlueprint orderBlueprint = createOrderBlueprint(orderBlueprintId);
        orderBlueprint.assignOrder(order);
        orderBlueprint.assignBlueprint(blueprint);
        return orderBlueprint;
    }

    public static void assignMemberToOrder(Order order, Member member) {
        order.assignMember(member);
    }

    public static void assignOrderMemberBlueprint(Order order, Member member, Blueprint blueprint, Long orderBlueprintId) {
        OrderBlueprint orderBlueprint = createAndAssignOrderBlueprint(orderBlueprintId, order, blueprint);
        assignMemberToOrder(order, member);
    }
}
