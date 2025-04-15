# RabbitMQ-Based Order Processing System

A simple microservices-based application built with Spring Boot that demonstrates asynchronous communication using RabbitMQ. It simulates an e-commerce order flow where users can place orders, save the order in database, and email notifications are sent.

## Services Overview
This project is divided into three microservices


### 1. **Order Service**
- Accepts order placement requests.
- Sends order details to RabbitMQ exchange order-exchange, which routes the message to order-queue.

### 2. **Inventory Service**
- Listens to order-queue.
- Saves the order in database in MySQL.
- Publishes an event to notification-queue after processing.
- If order saving fails, the message is routed to the Dead Letter Queue (order-dlq).

### 3. **Notification Service**
- Listens to notification-queue.
- Sends a confirmation email to the user (mocked/logged).
- Also listens to order-dlq to send a failure email if order processing fails.

## Tech Stack
- Java 17+
- Spring Boot 3.x
- RabbitMQ
- Spring AMQP
- Spring Data JPA (MySQL)
- Lombok
- Postman (for testing)

## RabbitMQ QUEUES

| Name                 | Type    | Description                                 |
|----------------------|---------|---------------------------------------------|
| order-exchange       | Exchange | Produced by Order Service                   |
| order-queue          | Queue    | Consumed by Inventory Service               |
| order-dlq-exchange   | Exchange | Produced when Inventory processing fails    |
| order-dlq            | Queue    | Consumed by Notification Service on failure |
| notification-queue   | Queue    | Produced by Inventory Service               |




## Sample Payload (Place Order)
{
  "product": "Wireless Mouse",
  "quantity": 1,
  "status": "",
  "email": "manjeetsinghwins@gmail.com",
  "address": "D140 7 phase industrial area Mohali"
}
