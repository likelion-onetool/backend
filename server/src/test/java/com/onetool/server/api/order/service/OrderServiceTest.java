package com.onetool.server.api.order.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.fake.FakeOrderRepository;
import com.onetool.server.global.new_exception.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.onetool.server.api.blueprint.fixture.BlueprintFixture.*;
import static com.onetool.server.api.member.fixture.MemberFixture.*;
import static com.onetool.server.api.order.fixture.OrderFixture.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;
    private FakeOrderRepository orderRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        orderRepository = new FakeOrderRepository();
        orderService = new OrderService(orderRepository);
        initTestOrders();
    }

    private void initTestOrders() {
        Member member = createMember();
        Order order1 = createOrder(1L);
        Order order2 = createOrder(2L);
        Order order3 = createOrder(3L);

        Stream.of(order1, order2, order3).forEach(order -> {
            order.assignMember(member);
            orderRepository.save(order);
        });

        this.member = member;
    }

    @Test
    void 사용자의_주문목록을_오름차순_페이징_조회한다() {
        // ✅ Given (설정)
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending()); //3개의 객체를 등록하지만 2개의 갯수만 가져오는 페이지

        // ✅ When (실행)
        Page<Order> orders = orderService.findAll(member.getId(), pageable);

        // ✅ Then (검증)
        assertThat(orders).hasSize(2);
        assertThat(orders.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void 사용자의_주문목록을_내림차순_페이징_조회한다() {
        // ✅ Given (설정)
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").descending()); //3개의 객체를 등록하지만 2개의 갯수만 가져오는 페이지

        // ✅ When (실행)
        Page<Order> orders = orderService.findAll(member.getId(), pageable);

        // ✅ Then (검증)
        assertThat(orders).hasSize(2);
        assertThat(orders.getContent().get(0).getId()).isEqualTo(3L); //현재 데이터의 마지막 pk값이 3L이다.
    }


    @Test
    void 상품을_조회한다() {
        // ✅ Given (설정)
        Order order = orderRepository.findById(1L).get();

        // ✅ When (실행)
        Order findOrder = orderService.findOne(order.getId());

        // ✅ Then (검증)
        assertThat(findOrder.getId()).isEqualTo(1L);
    }

    @Test
    void 상품_조회시_해당_상품이_없으면_실패한다() {
        // ✅ Given (설정)
        Long nonexistentOrderId = 999L;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> orderService.findOne(nonexistentOrderId))
                .isInstanceOf(ApiException.class)
                .hasMessage("orderId : 999");
    }

    @Test
    void 상품을_저장한다() {
        // ✅ Given (설정)
        Order order = orderRepository.findById(1L).get();
        Blueprint bluePrint1 = createBluePrint(1L); //downLoadLink == Test.uri
        Blueprint bluePrint2 = createBluePrint(2L);

        // ✅ When (실행)
        Long orderId = orderService.saveOrder(order, member, List.of(bluePrint1, bluePrint2));
        Optional<Order> findOrder = orderRepository.findById(orderId);

        // ✅ Then (검증)
        assertThat(findOrder).isNotNull();
        assertThat(findOrder.get().getMember().getId()).isEqualTo(2L);
        assertThat(findOrder.get().getOrderItems().get(0).getDownloadUrl()).isEqualTo("Test.uri");
    }

    @Test
    void 상품_저장시_Order가_Null이면_에러를_발생한다() {
        // ✅ Given (설정)
        Order order = null;
        Member member = createMember();//id ==2L
        Blueprint bluePrint1 = createBluePrint(1L); //downLoadLink == Test.uri
        Blueprint bluePrint2 = createBluePrint(2L);

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> orderService.saveOrder(order, member, List.of(bluePrint1, bluePrint2)))
                .isInstanceOf(ApiException.class)
                .hasMessage("Orders 객체가 NULL입니다.");

    }

    @Test
    void 주문목록을_삭제한다() {
        // ✅ Given (설정)
        Order order = orderRepository.findById(1L).get();

        // ✅ When (실행)
        orderService.deleteOrder(order);
        List<Order> orders = orderService.findAll(member.getId());

        // ✅ Then (검증)
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void 주문_객체가_NULL이면_에러를_발생한다() {
        // ✅ Given (설정)
        Order order = null;

        // ✅ When (실행) && ✅ Then (검증)
        assertThatThrownBy(() -> orderService.deleteOrder(order))
                .isInstanceOf(ApiException.class)
                .hasMessage("Orders 객체가 NULL입니다.");
    }

    @Test
    void 유저의_모든_주문목록을_조회한다() {
        // ✅ Given (설정)
        Long userId = member.getId();

        // ✅ When (실행)
        List<Order> resultOrders = orderService.findAll(userId);

        // ✅ Then (검증)
        assertThat(resultOrders).hasSize(3);
        assertThat(resultOrders.get(0).getId()).isEqualTo(1L);
    }
}
