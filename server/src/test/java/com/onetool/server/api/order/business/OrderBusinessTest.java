package com.onetool.server.api.order.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.fixture.BlueprintFixture;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.fixture.MemberFixture;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.OrderBlueprint;
import com.onetool.server.api.order.dto.response.MyPageOrderResponse;
import com.onetool.server.api.order.fixture.OrderBlueprintFixture;
import com.onetool.server.api.order.fixture.OrderFixture;
import com.onetool.server.api.order.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static com.onetool.server.Util.AssignUtil.assignOrderMemberBlueprint;
import static com.onetool.server.api.blueprint.fixture.BlueprintFixture.*;
import static com.onetool.server.api.member.fixture.MemberFixture.*;
import static com.onetool.server.api.order.fixture.OrderFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderBusinessTest {
    @Mock
    private OrderService orderService;
    @Mock
    private MemberService memberService;
    @Mock
    private BlueprintService blueprintService;

    @InjectMocks
    private OrderBusiness orderBusiness;

    @Test
    void 주문을_추가한다() {
        // ✅ Given (설정)
        Member member = createMember();
        String userEmail = "user@example.com";
        Set<Long> blueprintIdx = Set.of(1L, 2L);

        Blueprint bluePrint1 = createBluePrint(1L);
        Blueprint bluePrint2 = createBluePrint(2L);

        List<Blueprint> bluePrintList = List.of(bluePrint1, bluePrint2);

        when(memberService.findOne(userEmail)).thenReturn(member);
        when(blueprintService.findAllBlueprintByIds(blueprintIdx)).thenReturn(bluePrintList);
        when(orderService.saveOrder(any(Order.class), eq(member), eq(bluePrintList))).thenReturn(100L);

        // ✅ When (실행)
        Long result = orderBusiness.createOrder(userEmail, blueprintIdx);

        // ✅ Then (검증)
        assertThat(result).isEqualTo(100L);
        verify(orderService).saveOrder(any(Order.class), eq(member), eq(bluePrintList));
    }

    @Test
    void 유저의_주문목록을_보여준다() {
        // ✅ Given (설정)
        Long userId = 2L;
        Pageable pageable = PageRequest.of(0, 10);

        Member member = createMember();
        Order order1 = createOrder(1L);
        Order order2 = createOrder(2L);

        Blueprint bluePrint1 = createBluePrint(1L);
        Blueprint bluePrint2 = createBluePrint(2L);
        List<Blueprint> blueprintList = List.of(bluePrint1, bluePrint2);

        assignOrderMemberBlueprint(order1, member, bluePrint1, 1L);
        assignOrderMemberBlueprint(order2, member, bluePrint2, 2L);

        List<Order> orders = List.of(order1, order2); // Ensure orders is not empty
        Page<Order> ordersList = new PageImpl<>(orders);

        // ✅ When (실행)
        when(memberService.findOneWithCart(userId)).thenReturn(member);
        when(orderService.findAll(member.getId(), pageable)).thenReturn(ordersList);
        when(blueprintService.findAll()).thenReturn(blueprintList);

        List<MyPageOrderResponse> myPageOrderResponseList = orderBusiness.getMyPageOrderResponseList(userId, pageable);
        // ✅ Then (검증)
        assertThat(myPageOrderResponseList.size()).isEqualTo(2);
        assertThat(myPageOrderResponseList.get(0).orderId()).isEqualTo(1L);
    }

    @Test
    void 주문을_삭제한다() {
        // ✅ Given (설정)
        Order order = createOrder(1L);
        when(orderService.findOne(order.getId())).thenReturn(order);

        // ✅ When (실행)
        orderBusiness.removeOrder(1L);

        // ✅ Then (검증)
        verify(orderService).deleteOrder(order);
    }
}
