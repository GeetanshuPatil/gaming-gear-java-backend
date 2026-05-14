package com.geetanshu.gaminggearshop.controller;

import com.geetanshu.gaminggearshop.entity.Cart;
import com.geetanshu.gaminggearshop.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ✅ GET USER CART
    @GetMapping
    public Cart getCart() {

        return cartService.getCart();
    }
    // ✅ ADD PRODUCT TO CART
    @PostMapping("/add/{productId}")
    public Cart addToCart(@PathVariable Long productId) {

        return cartService.addToCart(productId);
    }
    @PutMapping("/increase/{productId}")
    public Cart increaseQuantity(@PathVariable Long productId) {
        return cartService.increaseQuantity(productId);
    }

    @PutMapping("/decrease/{productId}")
    public Cart decreaseQuantity(@PathVariable Long productId) {
        return cartService.decreaseQuantity(productId);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart removeItem(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }

    @DeleteMapping("/clear")
    public Cart clearCart() {
        return cartService.clearCart();
    }


}