package com.sparta.basic6.infrastructure.jpa;

import com.sparta.basic6.domain.Order;
import com.sparta.basic6.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepository {
}
