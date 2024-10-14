package com.sparta.basic6.application.order.dtos.message;

import com.sparta.basic6.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    Long orderId;
    OrderStatus orderStatus;
}
