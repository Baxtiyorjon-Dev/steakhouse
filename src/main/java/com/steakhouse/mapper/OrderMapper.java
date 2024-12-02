package com.steakhouse.mapper;

import com.steakhouse.dto.OrderItemRequest;
import com.steakhouse.dto.OrderRequest;
import com.steakhouse.model.Order;
import com.steakhouse.model.OrderItem;
import com.steakhouse.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "productId", target = "product")
    OrderItem toEntity(OrderItemRequest orderItemRequest);

    default Product map(Long value) {
        Product product = new Product();
        product.setId(value);
        return product;
    }
}
