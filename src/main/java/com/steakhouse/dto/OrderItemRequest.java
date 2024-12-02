package com.steakhouse.dto;

import jakarta.validation.constraints.Min;
import org.antlr.v4.runtime.misc.NotNull;


public class OrderItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private String size;

    @NotNull
    @Min(1)
    private Integer quantity;

    // Getters and Setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public @Min(1) Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@Min(1) Integer quantity) {
        this.quantity = quantity;
    }
}
