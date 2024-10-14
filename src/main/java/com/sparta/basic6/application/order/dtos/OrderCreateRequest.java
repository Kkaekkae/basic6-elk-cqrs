package com.sparta.basic6.application.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    List<ProductCreateRequest> products;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductCreateRequest {
        String name;
        Long price;
        Integer quantity;
        List<String> images;
    }
}
