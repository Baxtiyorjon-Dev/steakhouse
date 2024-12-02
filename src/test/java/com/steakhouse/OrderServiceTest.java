package com.steakhouse;

import com.steakhouse.exception.ResourceNotFoundException;
import com.steakhouse.model.*;
import com.steakhouse.repository.OrderRepository;
import com.steakhouse.repository.OrderItemRepository;
import com.steakhouse.service.DiscountService;
import com.steakhouse.service.OrderService;
import com.steakhouse.service.ProductService;
import com.steakhouse.service.TaxService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TaxService taxService;

    @Mock
    private DiscountService discountService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrder() {
        // Initialize Order, OrderItem, and Product objects
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        Product product = new Product();

        // Set up Product with a valid size-price mapping
        product.setSizes(Collections.singletonMap("M", new BigDecimal("10.00")));

        // Link Product and OrderItem
        orderItem.setProduct(product);
        orderItem.setSize("M");
        orderItem.setQuantity(2);

        // Mock dependencies to return valid values
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(discountService.calculateDiscount(any())).thenReturn(new BigDecimal("2.00"));
//        when(taxService.calculateTax(any(), anyString())).thenReturn(new BigDecimal("1.00"));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Execute the method being tested
        Order result = orderService.placeOrder(order, List.of(orderItem));

        // Assertions to validate results
        assertNotNull(result);
        assertEquals(new BigDecimal("20.00"), result.getSubtotalAmount());  // subtotal = 2 * 10.00
        assertEquals(new BigDecimal("2.00"), result.getDiscountAmount());
        assertEquals(new BigDecimal("1.00"), result.getTaxAmount());
        assertEquals(new BigDecimal("19.00"), result.getTotalAmount()); // total = subtotal - discount + tax

        // Verify that the orderItem was saved
        verify(orderItemRepository, times(1)).save(orderItem);
    }


    @Test
    void getOrderById_OrderExists_ReturnsOrder() {
        Order order = new Order();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void getOrderById_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }
}
