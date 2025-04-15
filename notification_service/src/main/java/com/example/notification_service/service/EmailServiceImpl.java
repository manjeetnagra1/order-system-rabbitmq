package com.example.notification_service.service;

import com.example.configurations.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getEmail());
        message.setSubject("Order Confirmation - " + order.getOrderId());
        if (order.getStatus().equals("failed")){
            message.setSubject("Order failed");
        }
        message.setText(buildEmailBody(order, order.getStatus()));

        mailSender.send(message);
        log.info("Email sent to: {}", order.getEmail());
    }

    private String buildEmailBody(Order order, String status) {
        if ("failed".equals(status)) {
            return "Hi " + order.getEmail() + ",\n\n" +
                    "We’re sorry, but we couldn’t process your order at this time.\n\n" +
                    "Product: " + order.getProduct() + "\n" +
                    "Quantity: " + order.getQuantity() + "\n" +
                    "Best regards,\n" +
                    "Customer Support Team";
        }

        return "Hi " + order.getEmail() + ",\n\n" +
                "Thank you for your order!\n\n" +
                "Order ID: " + order.getOrderId() + "\n" +
                "Product: " + order.getProduct() + "\n" +
                "Quantity: " + order.getQuantity() + "\n" +
                "Shipping Address: " + order.getAddress() + "\n\n" +
                "We will notify you once your order is shipped.\n\n" +
                "Best regards,\n" +
                "Customer Support Team";
    }

}
