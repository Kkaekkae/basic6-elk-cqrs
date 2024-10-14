package com.sparta.basic6.application.order.service;

import com.sparta.basic6.application.order.dtos.OrderCreateRequest;
import com.sparta.basic6.application.order.dtos.OrderDetailResponse;
import com.sparta.basic6.application.order.dtos.message.OrderMessage;
import com.sparta.basic6.application.order.service.message.OrderProduceService;
import com.sparta.basic6.domain.Order;
import com.sparta.basic6.domain.OrderProduct;
import com.sparta.basic6.domain.ProductImage;
import com.sparta.basic6.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class OrderCommandService {
    private final OrderRepository orderRepository;
    private final OrderProduceService messageService;

    public OrderCommandService(OrderRepository orderRepository, OrderProduceService messageService) {
        this.orderRepository = orderRepository;
        this.messageService = messageService;
    }

    @Transactional
    public OrderDetailResponse createOrder(OrderCreateRequest request) {
        Order order = Order.create();
        List<OrderProduct> orderProducts = getOrderProductsByRequest(request, order);
        order.updateProducts(orderProducts);
        orderRepository.save(order);

        // 트랜잭션이 완료된 후에 메서드 호출
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                messageService.sendOrderCreateAfterEvent(order.getId(), new OrderMessage(order.getId(), order.getOrderStatus()));
            }
        });
        return OrderDetailResponse.of(order);
    }

    private List<OrderProduct> getOrderProductsByRequest(OrderCreateRequest request, final Order order) {
        return request.getProducts().stream().map(productRequest -> {
            OrderProduct product = OrderProduct.create(
                    order,
                    productRequest.getName(),
                    productRequest.getPrice(),
                    productRequest.getQuantity());

            List<ProductImage> images = productRequest.getImages().stream()
                    .map(url -> ProductImage.create(product, url))
                    .toList();
            product.updateImages(images);
            return product;
        }).toList();
    }
}
