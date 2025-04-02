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
    private final SearchOrderService searchOrderService;

    public OrderQueryService(SearchOrderService searchOrderService) {
        this.searchOrderService = searchOrderService;
    }


    public List<OrderSearchResponse> getOrders(String productName, Long startPrice, Long endPrice) {
        return searchOrderService.search(productName, startPrice, endPrice);
    }
}
