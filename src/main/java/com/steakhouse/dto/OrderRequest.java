package com.steakhouse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.List;

public class OrderRequest {

    @NotNull
    @Size(min = 1)
    private List<@Valid OrderItemRequest> orderItems;

    @NotNull
    private String region;

    @NotNull
    private LocalDate orderDate;

    // Getters and Setters

    public @Size(min = 1) List<@Valid OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(@Size(min = 1) List<@Valid OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
