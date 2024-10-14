package com.sparta.basic6.application.order.service;

import com.sparta.basic6.application.order.SearchOrderService;
import com.sparta.basic6.application.order.dtos.OrderSearchResponse;
import com.sparta.basic6.domain.repository.OrderRepository;
import com.sparta.basic6.infrastructure.elasticsearch.SearchOrder;
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
}
