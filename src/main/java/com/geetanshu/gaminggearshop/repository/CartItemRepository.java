package com.geetanshu.gaminggearshop.repository;

import com.geetanshu.gaminggearshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}