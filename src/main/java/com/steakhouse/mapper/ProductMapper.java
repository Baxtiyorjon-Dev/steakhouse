package com.steakhouse.mapper;

import com.steakhouse.dto.ProductDTO;
import com.steakhouse.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
            @Mapping(source = "category.id", target = "category") // Map Category's id to ProductDTO's category
    })
    ProductDTO toDTO(Product product);

    @Mappings({
            @Mapping(target = "category", ignore = true) // Handle Category mapping separately in the service layer
    })
    Product toEntity(ProductDTO productDTO);
}
