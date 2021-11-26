package com.brestore.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brestore.ecommerce.model.OrderItem;

public interface OrderItemsRepository extends JpaRepository<OrderItem,Integer> {
}
