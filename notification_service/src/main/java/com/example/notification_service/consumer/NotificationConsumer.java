package com.example.notification_service.consumer;

import com.example.configurations.configuration.RabbitConfig;
import com.example.configurations.model.Order;
import com.example.notification_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    private final EmailService service;

    public NotificationConsumer(EmailService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void notificationConsumer(Order order){
        try {
            service.sendEmail(order);
            log.info("Received notification event: {}", order);
        } catch (Exception e){
            log.error("Error: ", e);
        }
    }
}
