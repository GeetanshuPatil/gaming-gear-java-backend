package com.geetanshu.gaminggearshop.controller;

import com.geetanshu.gaminggearshop.entity.Wishlist;
import com.geetanshu.gaminggearshop.service.WishlistService;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/wishlist")
    @CrossOrigin("*")

public class WishlistController {

        private final WishlistService wishlistService;

        public WishlistController(WishlistService wishlistService) {
            this.wishlistService = wishlistService;
        }

        @PostMapping("/add/{productId}")
        public Wishlist addToWishlist(@PathVariable Long productId) {
            return wishlistService.addToWishlist(productId);
        }

        @GetMapping
        public Wishlist getWishlist() {
            return wishlistService.getWishlist();
        }

        @DeleteMapping("/remove/{productId}")
        public Wishlist removeItem(@PathVariable Long productId) {
            return wishlistService.removeItem(productId);
        }

        @DeleteMapping("/clear")
        public Wishlist clearWishlist() {
            return wishlistService.clearWishlist();
        }
}

