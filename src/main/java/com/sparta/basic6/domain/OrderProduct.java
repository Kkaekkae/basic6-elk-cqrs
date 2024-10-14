package com.sparta.basic6.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    public Order order;

    public String name;
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    public List<ProductImage> images;
    public Long price;
    public Integer quantity;

    public static OrderProduct create(Order order, String name, Long price, Integer quantity) {
        return OrderProduct.builder()
                .order(order)
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }

    public void updateImages(List<ProductImage> images) {
        this.images = images;
    }
}
