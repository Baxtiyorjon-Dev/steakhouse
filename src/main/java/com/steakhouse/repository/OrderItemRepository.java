package com.steakhouse.repository;

import com.steakhouse.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom query methods if needed
}
