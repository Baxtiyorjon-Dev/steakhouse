package com.steakhouse.controller;

import com.steakhouse.dto.ApiResponse;
import com.steakhouse.dto.OrderRequest;
import com.steakhouse.mapper.OrderMapper;
import com.steakhouse.model.Order;
import com.steakhouse.model.OrderItem;
import com.steakhouse.model.User;
import com.steakhouse.service.OrderService;
import com.steakhouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> placeOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());

        Order order = orderMapper.toEntity(orderRequest);
        order.setUser(user);

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(orderMapper::toEntity)
                .collect(Collectors.toList());

        Order savedOrder = orderService.place(order, orderItems);

        ApiResponse<Order> response = new ApiResponse<>("success", savedOrder, "Order placed successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Order> orders = orderService.getOrdersByUser(user);
        ApiResponse<List<Order>> response = new ApiResponse<>("success", orders, "Orders retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id, Authentication authentication) {
        Order order = orderService.getOrderById(id);
        User user = userService.findByUsername(authentication.getName());

        if (!order.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ApiResponse<Order> response = new ApiResponse<>("success", order, "Order retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
