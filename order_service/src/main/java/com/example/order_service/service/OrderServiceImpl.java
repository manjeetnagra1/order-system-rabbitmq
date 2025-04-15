package com.example.order_service.service;

import com.example.configurations.configuration.RabbitConfig;
import com.example.order_service.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final RabbitTemplate template;

    public OrderServiceImpl(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void placeOrder(Order order) {
        try {
            template.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, order);
            log.info("Order sent to Order Exchange: {}", order);
        } catch (Exception e){
            log.error("An Exception occurred: {}", order);
        }
    }
}
