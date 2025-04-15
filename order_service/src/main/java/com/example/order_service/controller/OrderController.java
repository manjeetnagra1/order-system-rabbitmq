package com.example.order_service.controller;

import com.example.order_service.model.Order;
import com.example.order_service.service.OrderService;
import com.example.order_service.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    ResponseDTO dto = new ResponseDTO();

    @PostMapping("/sendOrder")
    public ResponseEntity<ResponseDTO> placeOrder(@RequestBody Order order) {
        try {
            service.placeOrder(order);
            dto.setMessage("Order has been processed you will receive a confirmation email shortly");
            dto.setStatus(200);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Internal Server Error: {}", e.getMessage(), e);
            dto.setMessage("Internal Server Error");
            return ResponseEntity.status(500).body(dto);
        }
    }
}
