package com.example.inventory_service.service;

import com.example.configurations.configuration.RabbitConfig;
import com.example.inventory_service.model.Order;
import com.example.inventory_service.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public OrderServiceImpl(OrderRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void saveOrder(Order order) {
        try {
            repository.save(order);
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.NOTIFICATION_ROUTING_KEY,order);
            log.info("Message sent to Notification Queue for email confirmation: {}", order);
        }catch (Exception e){
            log.info("Error occurred while saving the order");
            throw new AmqpRejectAndDontRequeueException("Error processing order", e);
        }
    }
}
