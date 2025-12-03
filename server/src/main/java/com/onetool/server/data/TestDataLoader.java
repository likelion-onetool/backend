package com.onetool.server.data;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.cart.CartBlueprint;
import com.onetool.server.api.cart.repository.CartBlueprintRepository;
import com.onetool.server.api.cart.repository.CartRepository;
import com.onetool.server.api.category.FirstCategory;
import com.onetool.server.api.category.FirstCategoryRepository;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberJpaRepository;
import com.onetool.server.api.order.Orders;
import com.onetool.server.api.order.OrderBlueprint;
import com.onetool.server.api.order.repository.OrderBlueprintRepository;
import com.onetool.server.api.order.repository.OrderRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Profile("test")
@Component
public class TestDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    private final BlueprintRepository blueprintRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final FirstCategoryRepository firstCategoryRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartBlueprintRepository cartBlueprintRepository;
    private final OrderBlueprintRepository orderBlueprintRepository;

    public TestDataLoader(BlueprintRepository blueprintRepository, MemberJpaRepository memberJpaRepository, PasswordEncoder passwordEncoder, FirstCategoryRepository firstCategoryRepository, CartRepository cartRepository, OrderRepository orderRepository, CartBlueprintRepository cartBlueprintRepository, OrderBlueprintRepository orderBlueprintRepository) {
        this.blueprintRepository = blueprintRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.firstCategoryRepository = firstCategoryRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.cartBlueprintRepository = cartBlueprintRepository;
        this.orderBlueprintRepository = orderBlueprintRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        firstCategoryRepository.saveAll(Arrays.asList(
                FirstCategory.builder().id(1L).name("building").build(),
                FirstCategory.builder().id(2L).name("civil").build(),
                FirstCategory.builder().id(3L).name("interior").build(),
                FirstCategory.builder().id(4L).name("machine").build(),
                FirstCategory.builder().id(5L).name("electric").build(),
                FirstCategory.builder().id(6L).name("etc").build()
        ));

        for (int i = 1; i <= 20; i++) {
            // 1. Create Member
            Member member = memberJpaRepository.save(
                    Member.builder()
                            .email("test" + i + "@test.com")
                            .password(passwordEncoder.encode("password"))
                            .name("User " + i)
                            .build()
            );

            // 2. Create Blueprint
            Blueprint blueprint = blueprintRepository.save(
                    Blueprint.builder()
                            .blueprintName("테스트 마을 " + i)
                            .blueprintDetails("테스트 마을의 청사진입니다.")
                            .creatorName("작가 " + i)
                            .standardPrice(50000L)
                            .salePrice(40000L)
                            .program("CAD")
                            .downloadLink("https://onetool.com/download")
                            .extension(".dwg")
                            .blueprintImg("https://s3.bucket.image.com/")
                            .categoryId((long) (i % 6 + 1))
                            .secondCategory("주거")
                            .inspectionStatus(InspectionStatus.PASSED)
                            .build()
            );

            // 3. Create Cart and Order for the Member
            Cart cart = cartRepository.save(Cart.builder().member(member).build());
            Orders order = orderRepository.save(Orders.builder().member(member).build());

            // 4. Create CartBlueprint
            cartBlueprintRepository.save(
                    CartBlueprint.builder()
                            .cart(cart)
                            .blueprint(blueprint)
                            .build()
            );

            // 5. Create OrderBlueprint
            orderBlueprintRepository.save(
                    OrderBlueprint.builder()
                            .order(order)
                            .blueprint(blueprint)
                            .build()
            );
        }
    }
}
