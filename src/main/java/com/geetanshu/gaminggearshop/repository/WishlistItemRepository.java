package com.geetanshu.gaminggearshop.repository;

import com.geetanshu.gaminggearshop.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
}