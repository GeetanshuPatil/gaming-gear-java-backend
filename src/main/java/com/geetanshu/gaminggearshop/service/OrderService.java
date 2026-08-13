package com.geetanshu.gaminggearshop.service;

import com.geetanshu.gaminggearshop.dto.OrderRequest;
import com.geetanshu.gaminggearshop.entity.Cart;
import com.geetanshu.gaminggearshop.entity.CartItem;
import com.geetanshu.gaminggearshop.entity.Order;
import com.geetanshu.gaminggearshop.entity.OrderItem;
import com.geetanshu.gaminggearshop.entity.User;
import com.geetanshu.gaminggearshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(OrderRequest request) {

        // Get logged-in user's cart
        Cart cart = cartService.getCart();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = cart.getUser();

        // Calculate subtotal from database products
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            BigDecimal price =
                    BigDecimal.valueOf(cartItem.getProduct().getPrice());

            BigDecimal itemTotal =
                    price.multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    );

            subtotal = subtotal.add(itemTotal);
        }

        // Calculate shipping
        BigDecimal shippingFee;

        if (subtotal.compareTo(BigDecimal.valueOf(5000)) > 0) {
            shippingFee = BigDecimal.ZERO;
        } else {
            shippingFee = BigDecimal.valueOf(99);
        }

        BigDecimal totalAmount = subtotal.add(shippingFee);

        // Create order
        Order order = new Order();

        order.setUser(user);

        order.setPaymentMethod(request.getPaymentMethod());

        order.setRazorpayOrderId(request.getRazorpayOrderId());

        order.setRazorpayPaymentId(request.getRazorpayPaymentId());

        order.setSubtotal(subtotal);

        order.setShippingFee(shippingFee);

        order.setTotalAmount(totalAmount);

        order.setFullName(request.getFullName());

        order.setPhone(request.getPhone());

        order.setAddressLine(request.getAddressLine());

        order.setCity(request.getCity());

        order.setPincode(request.getPincode());

        order.setCreatedAt(LocalDateTime.now());

        // Payment status
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            order.setPaymentStatus("PENDING");
        } else {
            order.setPaymentStatus("PAID");
        }

        // Order status
        order.setStatus("PLACED");

        // Copy cart items into order items
        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProduct(cartItem.getProduct());

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setPrice(
                    BigDecimal.valueOf(
                            cartItem.getProduct().getPrice()
                    )
            );

            order.getItems().add(orderItem);
        }

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart only after order is successfully saved
        cartService.clearCart();

        return savedOrder;
    }
}