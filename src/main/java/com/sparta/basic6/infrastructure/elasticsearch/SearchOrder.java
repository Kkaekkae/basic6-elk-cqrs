package com.sparta.basic6.infrastructure.elasticsearch;

import com.sparta.basic6.application.order.dtos.OrderDetailResponse;
import com.sparta.basic6.domain.Order;
import com.sparta.basic6.domain.OrderProduct;
import com.sparta.basic6.domain.OrderStatus;
import com.sparta.basic6.domain.ProductImage;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Document(indexName = "order")
public class SearchOrder {
    @Id
    private String id; // ES 에서 자동으로 고유한 ID 를 생성해줍니다.
    private Long orderId;
    private Long totalPrice;
    @Field(name = "product_list") // Field 를 설정하지 않으면 Java 변수명이 자동으로 필드명으로 치환됩니다.
    private List<SearchProduct> products;
    @Field(name = "order_status", type = FieldType.Keyword)
    private OrderStatus status;
    @Field(type = FieldType.Date)
    private LocalDate createdAt;
    @Field(name = "expected_time", type = FieldType.Date_Range)
    private Range<LocalDate> expectedDeliveryTime;

    public LocalDate getExpectedStartDate() {
            return Optional.ofNullable(expectedDeliveryTime).map(time ->
                    time.getLowerBound().getValue().orElse(LocalDate.now())
            ).orElse(null);

    }

    public LocalDate getExpectedEndDate() {
        return Optional.ofNullable(expectedDeliveryTime).map(time ->
                time.getUpperBound().getValue().orElse(LocalDate.now())
        ).orElse(null);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Builder(access = AccessLevel.PRIVATE)
    public static class SearchProduct {
        private String name;
        private List<String> images;
        private Long price;
        private Integer quantity;

        public static SearchProduct of(OrderProduct product) {
            return SearchProduct.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .images(product.getImages().stream().map(ProductImage::getUrl).toList())
                    .build();
        }
    }



    public static SearchOrder of(Order order) {
        return SearchOrder.builder()
                .orderId(order.getId())
                .totalPrice(order.getProducts().stream().mapToLong(OrderProduct::getPrice).sum())
                .status(order.getOrderStatus())
                .createdAt(order.getCreatedAt().toLocalDate())
                .products(order.getProducts().stream().map(SearchProduct::of).toList())
                .expectedDeliveryTime(Range.of(
                        Range.Bound.exclusive(order.getExpectedDeliveryStartDate().toLocalDate()),
                        Range.Bound.exclusive(order.getExpectedDeliveryEndDate().toLocalDate())
                ))
                .build();
    }
}
