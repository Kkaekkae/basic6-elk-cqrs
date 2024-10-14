package com.sparta.basic6.userInterface;

import com.sparta.basic6.application.order.dtos.OrderCreateRequest;
import com.sparta.basic6.application.order.dtos.OrderDetailResponse;
import com.sparta.basic6.application.order.dtos.OrderSearchResponse;
import com.sparta.basic6.application.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderSearchResponse>> getOrders(
            @RequestParam(value = "product") String productName,
            @RequestParam(value = "start_price", required = false) Long startPrice,
            @RequestParam(value = "end_price", required = false) Long endPrice
    ) {
        List<OrderSearchResponse> response = orderService.getOrders(productName, startPrice, endPrice);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrder(
            @RequestBody OrderCreateRequest request
    ) {
        OrderDetailResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }
}
