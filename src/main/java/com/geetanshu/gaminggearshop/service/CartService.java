package com.geetanshu.gaminggearshop.service;

import com.geetanshu.gaminggearshop.entity.Cart;
import com.geetanshu.gaminggearshop.entity.CartItem;
import com.geetanshu.gaminggearshop.entity.Product;
import com.geetanshu.gaminggearshop.entity.User;
import com.geetanshu.gaminggearshop.repository.CartItemRepository;
import com.geetanshu.gaminggearshop.repository.CartRepository;
import com.geetanshu.gaminggearshop.repository.ProductRepository;
import com.geetanshu.gaminggearshop.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ✅ ADD TO CART
    public Cart addToCart(Long productId) {

        // 🔐 get logged-in user email from JWT
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        // 👤 find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🛒 get existing cart or create new one
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 📦 find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔁 check if product already exists in cart
        for (CartItem item : cart.getItems()) {

            if (item.getProduct().getId().equals(productId)) {

                item.setQuantity(item.getQuantity() + 1);

                return cartRepository.save(cart);
            }
        }

        // ➕ create new cart item
        CartItem cartItem = new CartItem();

        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setCart(cart);

        cart.getItems().add(cartItem);

        return cartRepository.save(cart);
    }

    // ✅ GET USER CART
    public Cart getCart() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
    }
    public Cart increaseQuantity( Long productId) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(item.getQuantity() + 1);

        cartItemRepository.save(item);

        return cartRepository.save(cart);
    }
    public Cart decreaseQuantity( Long productId) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(item.getQuantity() - 1);

        if (item.getQuantity() <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            cartItemRepository.save(item);
        }

        return cartRepository.save(cart);
    }
    public Cart removeItem( Long productId) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cart.getItems().remove(item);

        cartItemRepository.delete(item);

        return cartRepository.save(cart);
    }
    public Cart clearCart() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteAll(cart.getItems());

        cart.getItems().clear();

        return cartRepository.save(cart);
    }
}