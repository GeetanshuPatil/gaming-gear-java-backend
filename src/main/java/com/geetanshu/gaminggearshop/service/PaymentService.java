package com.geetanshu.gaminggearshop.service;

import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Value;
import com.geetanshu.gaminggearshop.dto.PaymentOrderResponse;
import com.geetanshu.gaminggearshop.dto.PaymentVerificationRequest;
import com.geetanshu.gaminggearshop.entity.Cart;
import com.geetanshu.gaminggearshop.entity.CartItem;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    private final RazorpayClient razorpayClient;
    private final CartService cartService;

    public PaymentService(
            RazorpayClient razorpayClient,
            CartService cartService
    ) {
        this.razorpayClient = razorpayClient;
        this.cartService = cartService;
    }

    public PaymentOrderResponse createPaymentOrder() throws Exception {

        Order order = createOrder();

        return new PaymentOrderResponse(
                order.get("id"),
                order.get("amount"),
                order.get("currency"),
                razorpayKeyId
        );
    }

    public Order createOrder() throws Exception {

        // Get logged-in user's cart
        Cart cart = cartService.getCart();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total from database product prices
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {

            Double price = item.getProduct().getPrice();

            BigDecimal itemTotal = BigDecimal.valueOf(price)
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);
        }

        // Shipping
        BigDecimal shippingFee;

        if (total.compareTo(BigDecimal.valueOf(5000)) > 0) {
            shippingFee = BigDecimal.ZERO;
        } else {
            shippingFee = BigDecimal.valueOf(99);
        }

        BigDecimal finalTotal = total.add(shippingFee);

        // Convert rupees to paise
        int amountInPaise = finalTotal
                .multiply(BigDecimal.valueOf(100))
                .intValueExact();
        

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put(
                "receipt",
                "gaminggear_receipt_" + System.currentTimeMillis()
        );

        return razorpayClient.orders.create(orderRequest);
    }

    public boolean verifyPayment(PaymentVerificationRequest request) {

        try {
            String payload =
                    request.getRazorpayOrderId()
                            + "|"
                            + request.getRazorpayPaymentId();

            String generatedSignature = Utils.getHash(
                    payload,
                    razorpayKeySecret
            );

            return generatedSignature.equals(request.getRazorpaySignature());

        } catch (Exception e) {
            return false;
        }
    }
}