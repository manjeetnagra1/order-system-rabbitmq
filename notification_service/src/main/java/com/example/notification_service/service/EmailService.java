package com.example.notification_service.service;

import com.example.configurations.model.Order;

public interface EmailService  {
    void sendEmail(Order order);
}
