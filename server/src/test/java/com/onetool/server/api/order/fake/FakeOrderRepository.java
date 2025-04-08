package com.onetool.server.api.order.fake;

import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.*;

public class FakeOrderRepository implements OrderRepository {
    private Long sequence = 1L;
    private final Map<Long, Order> store = new HashMap<>();

    @Override
    public Optional<Order> findById(Long id) {

        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Order save(Order order) {
        if(order.getId()==null){
            setIdByReflection(order,sequence++);
        }
        store.put(order.getId(),order);
        return order;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<Order> findAllByMemberId(Long memberId) {
        return store.values().stream()
                .filter(order-> order.getMember().getId().equals(memberId)
                ).toList();
    }

    @Override
    public Page<Order> findAllByMemberId(Long memberId, Pageable pageable) {
        List<Order> filtered = store.values().stream()
                .filter(order -> order.getMember().getId().equals(memberId))
                .sorted(getComparator(pageable.getSort()))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        List<Order> pageContent = filtered.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    private Comparator<Order> getComparator(Sort sort) {
        Comparator<Order> comparator = Comparator.comparing(Order::getId);

        for (Sort.Order order : sort) {
            if (order.getProperty().equals("id")) {
                Comparator<Order> idComparator = Comparator.comparing(Order::getId);
                comparator = order.isDescending() ? idComparator.reversed() : idComparator;
            }
        }
        return comparator;
    }

    private void setIdByReflection(Order order, Long id) {
        try {
            Field field = Order.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(order, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
