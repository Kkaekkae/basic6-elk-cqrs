package com.sparta.basic6.application.order.service.message;

import com.sparta.basic6.application.order.SearchOrderService;
import com.sparta.basic6.application.order.dtos.message.OrderMessage;
import com.sparta.basic6.domain.Order;
import com.sparta.basic6.domain.repository.OrderRepository;
import com.sparta.basic6.infrastructure.jpa.OrderElasticSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderConsumerService {

    private final OrderRepository orderRepository;
    private final SearchOrderService searchOrderService;

    public OrderConsumerService(OrderRepository orderRepository, SearchOrderService searchOrderService) {
        this.orderRepository = orderRepository;
        this.searchOrderService = searchOrderService;
    }

    @KafkaListener(groupId = "basic6", topics = "order_after_create")
    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @Transactional
    public void consumeOrderCreateMessage(OrderMessage orderMessage) {
        log.info("orderId: {} message consumed", orderMessage.getOrderId());
        Order order = orderRepository.findById(orderMessage.getOrderId()).orElseThrow();
        try {
            searchOrderService.create(order);
        }catch (Exception e) {
            log.error("{} 주문이 롤백되었습니다. {}", order.getId(), e.getMessage());
            orderRepository.delete(order); // Rollback
        }
    }
}
