package com.sparta.basic6.application.service;

import com.sparta.basic6.application.SearchOrderService;
import com.sparta.basic6.application.dtos.OrderDetailResponse;
import com.sparta.basic6.application.dtos.OrderSearchResponse;
import com.sparta.basic6.domain.Order;
import com.sparta.basic6.domain.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderQueryService {
    private final OrderRepository orderRepository;
    private final SearchOrderService searchOrderService;

    public OrderQueryService(OrderRepository orderRepository, SearchOrderService searchOrderService) {
        this.orderRepository = orderRepository;
        this.searchOrderService = searchOrderService;
    }


    public List<OrderSearchResponse> getOrders(String productName, Long startPrice, Long endPrice) {
        List<OrderSearchResponse> response = searchOrderService.search(productName, startPrice, endPrice);
        return response;
    }

    public OrderDetailResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderDetailResponse.of(order);
    }
}
