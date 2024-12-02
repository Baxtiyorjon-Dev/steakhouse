package com.steakhouse.dto;

import com.steakhouse.model.Category;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Long category;
    private Map<String, BigDecimal> sizes;
    private String imageUrl;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Map<String, BigDecimal> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, BigDecimal> sizes) {
        this.sizes = sizes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
