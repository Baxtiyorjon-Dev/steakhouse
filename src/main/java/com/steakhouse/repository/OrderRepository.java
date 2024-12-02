package com.steakhouse.repository;

import com.steakhouse.model.Order;
import com.steakhouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
