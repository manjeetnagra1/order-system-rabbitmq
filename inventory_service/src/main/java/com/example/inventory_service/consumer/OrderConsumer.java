package com.example.inventory_service.consumer;

import com.example.configurations.configuration.RabbitConfig;
import com.example.inventory_service.model.Order;
import com.example.inventory_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {


    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void getOrder(Order order){
        try {
            orderService.saveOrder(order);
            log.info("Order Received and Saved: {}", order);
        } catch (Exception e) {
            log.error("Failed to process order: {}", order, e);
            throw new AmqpRejectAndDontRequeueException("Error processing order", e);
        }
    }
}
