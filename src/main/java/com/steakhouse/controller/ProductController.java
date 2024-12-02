package com.steakhouse.controller;

import com.steakhouse.dto.ApiResponse;
import com.steakhouse.dto.ProductDTO;
import com.steakhouse.mapper.ProductMapper;
import com.steakhouse.model.Product;
import com.steakhouse.repository.CategoryRepository;
import com.steakhouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<?>>> getAllCategories() {
        List<?> categories = categoryRepository.findAll();
        ApiResponse<List<?>> response = new ApiResponse<>("success", categories, "Products retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<?> productDTOs = products.stream().map(product -> {
            Map<Object, Object> dto = new HashMap<>();
            dto.put("id", product.getId());
            dto.put("name", product.getName());
            dto.put("description", product.getDescription());
            dto.put("basePrice",product.getBasePrice());
            dto.put("sizes",product.getSizes());
            dto.put("imageUrl", product.getImageUrl());
            dto.put("category", product.getCategory().getName());
            return dto;
        }).toList();
        ApiResponse<List<?>> response = new ApiResponse<>("success", productDTOs, "Products retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductDTO productDTO = productMapper.toDTO(product);
        ApiResponse<?> response = new ApiResponse<>("success", productDTO, "Product retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
