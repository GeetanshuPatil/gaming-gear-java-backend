package com.geetanshu.gaminggearshop.controller;

import com.geetanshu.gaminggearshop.dto.OrderRequest;
import com.geetanshu.gaminggearshop.entity.Order;
import com.geetanshu.gaminggearshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest request
    ) {

        try {

            Order order = orderService.createOrder(request);

            return ResponseEntity.ok(order);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}