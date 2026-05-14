package com.geetanshu.gaminggearshop.service;

import com.geetanshu.gaminggearshop.entity.*;
import com.geetanshu.gaminggearshop.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            WishlistItemRepository wishlistItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ✅ ADD TO WISHLIST
    public Wishlist addToWishlist(Long productId) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // prevent duplicates
        for (WishlistItem item : wishlist.getItems()) {

            if (item.getProduct().getId().equals(productId)) {
                return wishlist;
            }
        }

        WishlistItem wishlistItem = new WishlistItem();

        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);

        wishlist.getItems().add(wishlistItem);

        return wishlistRepository.save(wishlist);
    }

    // ✅ GET USER WISHLIST
    public Wishlist getWishlist() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist is empty"));
    }

    // ✅ REMOVE ITEM
    public Wishlist removeItem(Long productId) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        WishlistItem item = wishlist.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        wishlist.getItems().remove(item);

        wishlistItemRepository.delete(item);

        return wishlistRepository.save(wishlist);
    }

    // ✅ CLEAR WISHLIST
    public Wishlist clearWishlist() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        wishlistItemRepository.deleteAll(wishlist.getItems());

        wishlist.getItems().clear();

        return wishlistRepository.save(wishlist);
    }
}