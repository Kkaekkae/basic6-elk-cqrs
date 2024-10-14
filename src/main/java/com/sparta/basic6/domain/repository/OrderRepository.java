package com.sparta.basic6.domain.repository;

import com.sparta.basic6.application.order.dtos.OrderCreateRequest;
import com.sparta.basic6.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Long id);
    Order save(Order order);

    void delete(Order order);
}
