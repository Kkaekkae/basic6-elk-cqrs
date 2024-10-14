package com.sparta.basic6.application.order.service.message;

import com.sparta.basic6.application.order.dtos.message.OrderMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProduceService {
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    public OrderProduceService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreateAfterEvent(Long key, OrderMessage message) {
        kafkaTemplate.send("order_after_create", key.toString(), message);
    }
}
