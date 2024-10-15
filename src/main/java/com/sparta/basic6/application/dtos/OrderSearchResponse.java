package com.sparta.basic6.application.dtos;

import com.sparta.basic6.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderSearchResponse {
    Long orderId;
    Long totalPrice;
    List<ProductResponse> products;
    OrderStatus status;
    LocalDate createdAt;
    LocalDate expectedDeliveryStartDate;
    LocalDate expectedDeliveryEndDate;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class ProductResponse {
        String name;
        List<String> images;
        Long price;
        Integer quantity;
    }
}
