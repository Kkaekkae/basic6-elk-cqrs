package com.sparta.basic6.application.order.service;

import com.sparta.basic6.application.order.dtos.OrderCreateRequest;
import com.sparta.basic6.application.order.dtos.OrderDetailResponse;
import com.sparta.basic6.application.order.dtos.OrderSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {
    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderService(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    public OrderDetailResponse createOrder(OrderCreateRequest request) {
        log.info("create order");
        return orderCommandService.createOrder(request);
    }

    public List<OrderSearchResponse> getOrders(String productName, Long startPrice, Long endPrice) {
        log.info("get customers productName: {}, startPrice: {}, endPrice: {}", productName, startPrice, endPrice);
        return orderQueryService.getOrders(productName, startPrice, endPrice);
    }

}
