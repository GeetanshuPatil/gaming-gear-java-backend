package com.geetanshu.gaminggearshop.controller;

import com.geetanshu.gaminggearshop.dto.PaymentOrderResponse;
import com.geetanshu.gaminggearshop.dto.PaymentVerificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.geetanshu.gaminggearshop.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder() {

        try {

            PaymentOrderResponse response =
                    paymentService.createPaymentOrder();

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Unable to create payment order: " + e.getMessage());
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerificationRequest request
    ) {

        boolean verified = paymentService.verifyPayment(request);

        if (verified) {
            return ResponseEntity.ok(
                    "Payment verified successfully"
            );
        }

        return ResponseEntity
                .badRequest()
                .body("Payment verification failed");
    }
}