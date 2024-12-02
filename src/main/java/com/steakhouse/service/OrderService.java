package com.steakhouse.service;

import com.steakhouse.exception.ResourceNotFoundException;
import com.steakhouse.model.Order;
import com.steakhouse.model.OrderItem;
import com.steakhouse.model.Product;
import com.steakhouse.model.User;
import com.steakhouse.repository.OrderItemRepository;
import com.steakhouse.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TaxService taxService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order place(Order order, List<OrderItem> orderItems) {

        Integer total = 0;
        for (OrderItem item : orderItems) {
            if (item.getProduct().getBasePrice() != null) {
                total += (item.getProduct().getBasePrice()).intValue();
            }
            orderItemRepository.save(item);
        }

        order.setOrderItems(orderItems);

        order.setTotalAmount(BigDecimal.valueOf(total));
        order.setOrderItems(orderItems);
        return order;
    }

    @Transactional
    public Order placeOrder(Order order, List<OrderItem> orderItems) {
        BigDecimal subtotal = BigDecimal.ZERO;

        // Calculate subtotal
        BigDecimal unitPrice = null;
        for (OrderItem item : orderItems) {
            Product product = productService.getProductById(item.getProduct().getId());

            // unitPrice qiymatini olish va null tekshiruvi
            unitPrice = product.getSizes().get(item.getSize());
//            if (unitPrice == null) {
//                throw new IllegalArgumentException("Unit price is not defined for the selected product and size.");
//            }

            item.setUnitPrice(item.getProduct().getBasePrice());
//            BigDecimal itemSubtotal = unitPrice.multiply(new BigDecimal(item.getQuantity()));
//            item.setSubtotal(itemSubtotal);
            item.setOrder(order);
//            subtotal = subtotal.add(itemSubtotal);
        }


        // Apply discounts
        BigDecimal discountAmount = discountService.calculateDiscount(subtotal);

        // Calculate tax
//        BigDecimal taxAmount = taxService.calculateTax(subtotal.subtract(discountAmount), order.getRegion());

        // Set order totals
        order.setSubtotalAmount(subtotal);
        order.setDiscountAmount(discountAmount);

        BigDecimal total = BigDecimal.valueOf(0);

        for (BigDecimal i : orderItems.stream().map(orderItem -> orderItem.getProduct().getBasePrice()).toList()) {
            total = total.add(i);
        }

        System.out.println(total);
//        order.setTaxAmount(taxAmount);
        order.setTotalAmount(total);
        order.setOrderItems(orderItems);
        order.setDeliveryEstimate(LocalDate.now().plusDays(3)); // Example delivery estimate

        Order savedOrder = orderRepository.save(order);

        // Save order items
        for (OrderItem item : orderItems) {
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
