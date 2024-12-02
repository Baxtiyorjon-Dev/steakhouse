package com.steakhouse.controller;

import com.steakhouse.dto.ApiResponse;
import com.steakhouse.dto.CategoryDTO;
import com.steakhouse.dto.ProductDTO;
import com.steakhouse.mapper.ProductMapper;
import com.steakhouse.model.Category;
import com.steakhouse.model.Order;
import com.steakhouse.model.Product;
import com.steakhouse.model.User;
import com.steakhouse.repository.CategoryRepository;
import com.steakhouse.service.OrderService;
import com.steakhouse.service.ProductService;
import com.steakhouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryRepository categoryRepository;

    // Product Management Endpoints

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setImageUrl(categoryDTO.getImageUrl());
        category = categoryRepository.save(category);
        ApiResponse<?> response = new ApiResponse<>("success", category, "Product created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategory()).get();
        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        Product savedProduct = productService.createProduct(product);
        ApiResponse<ProductDTO> response = new ApiResponse<>("success", productMapper.toDTO(savedProduct), "Product created successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductDTO productDTO) {

        Product productDetails = productMapper.toEntity(productDTO);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        ApiResponse<ProductDTO> response = new ApiResponse<>("success", productMapper.toDTO(updatedProduct), "Product updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        ApiResponse<String> response = new ApiResponse<>("success", null, "Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Order Management Endpoints

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        ApiResponse<List<Order>> response = new ApiResponse<>("success", orders, "Orders retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.registerUser(user);
        ApiResponse<User> response = new ApiResponse<>("success", createdUser, "User created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid User userDetails) {

        User updatedUser = userService.updateUser(id, userDetails);
        ApiResponse<User> response = new ApiResponse<>("success", updatedUser, "User updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ApiResponse<List<User>> response = new ApiResponse<>("success", users, "Users retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);  // Bu yerda UserService o'rniga userService ishlatiladi.
        ApiResponse<String> response = new ApiResponse<>("success", null, "User deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
