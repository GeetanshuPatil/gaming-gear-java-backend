package com.geetanshu.gaminggearshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // many wishlist items can reference same product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // many items belong to one wishlist
    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    @JsonBackReference
    private Wishlist wishlist;


    public WishlistItem() {
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }


}