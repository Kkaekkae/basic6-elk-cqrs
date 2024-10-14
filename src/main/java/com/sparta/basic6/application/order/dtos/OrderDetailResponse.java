package com.sparta.basic6.application.order.dtos;

import com.sparta.basic6.domain.Order;
import com.sparta.basic6.domain.OrderProduct;
import com.sparta.basic6.domain.OrderStatus;
import com.sparta.basic6.domain.ProductImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    OrderStatus orderStatus;
    List<ProductResponse> products;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Builder(access = AccessLevel.PRIVATE)
    public static class ProductResponse {
        String name;
        Long price;
        Integer quantity;
        List<String> images;

        public static ProductResponse of(OrderProduct product) {
            return ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .images(product.getImages().stream().map(ProductImage::getUrl).toList())
                    .build();
        }
    }

    public static OrderDetailResponse of(Order order) {
        return OrderDetailResponse.builder()
                .orderStatus(order.getOrderStatus())
                .products(order.getProducts().stream().map(ProductResponse::of).toList())
                .build();
    }
}
