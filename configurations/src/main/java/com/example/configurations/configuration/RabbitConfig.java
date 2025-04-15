package com.example.configurations.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String ORDER_QUEUE = "orderQueue";
    public static final String EXCHANGE_NAME = "orderExchange";
    public static final String ROUTING_KEY = "orderRoutingKey";
    public static final String NOTIFICATION_QUEUE = "notificationQueue";
    public static final String NOTIFICATION_ROUTING_KEY = "notificationRoutingKey";
    public static final String ORDER_DLQ = "order-dlq";
    public static final String ORDER_DLQ_EXCHANGE = "order-dlq-exchange";

    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", ORDER_DLQ_EXCHANGE);
        args.put("x-dead-letter-routing-key", ORDER_DLQ);
        return new Queue(ORDER_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange orderDlqExchange() {
        return new DirectExchange(ORDER_DLQ_EXCHANGE);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }


    @Bean
    public Queue orderDlqQueue() {
        return new Queue(ORDER_DLQ);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding orderDlqBinding() {
        return BindingBuilder.bind(orderDlqQueue()).to(orderDlqExchange()).with(ORDER_DLQ);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(notificationQueue).to(orderExchange).with(NOTIFICATION_ROUTING_KEY);
    }


    @Bean
    public Jackson2JsonMessageConverter jsonConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }

}
