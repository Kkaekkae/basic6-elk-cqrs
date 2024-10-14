package com.sparta.basic6.application.order;

import com.sparta.basic6.application.order.dtos.OrderSearchResponse;
import com.sparta.basic6.domain.Order;

import java.util.List;

public interface SearchOrderService {
    List<OrderSearchResponse> search(String productName, Long startPrice, Long endPrice);

    void create(Order order);
}
