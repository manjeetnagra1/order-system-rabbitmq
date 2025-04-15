package com.example.notification_service.consumer;

import com.example.configurations.configuration.RabbitConfig;
import com.example.configurations.model.Order;
import com.example.notification_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DLQConsumer {

    private final EmailService service;

    public DLQConsumer(EmailService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.ORDER_DLQ)
    public void getDlqMessages(Order order){
        log.error("Order processing failed: {}", order);
        order.setStatus("failed");
        service.sendEmail(order);
    }
}
