package com.onetool.server.api.payments.repository;

import com.onetool.server.api.order.Order;
import com.onetool.server.api.payments.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.order = :order")
    Payment findByOrders(@Param("order") Order order);
}
