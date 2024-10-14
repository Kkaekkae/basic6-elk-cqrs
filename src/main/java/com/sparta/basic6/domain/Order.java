package com.sparta.basic6.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    public List<OrderProduct> products;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expectedDeliveryStartDate;
    private LocalDateTime expectedDeliveryEndDate;

    public static Order create() {
        LocalDateTime now = LocalDateTime.now();
        return Order.builder()
                .orderStatus(OrderStatus.LOADING_PRODUCT)
                .createdAt(now)
                .expectedDeliveryStartDate(now.plusDays(1)) // 최소 배송일 = 주문일자 +1
                .expectedDeliveryEndDate(now.plusDays(3)) // 최대 배송일 = 주문일자 +3
                .build();
    }

    public void updateProducts(List<OrderProduct> products) {
        this.products = products;
    }
}
