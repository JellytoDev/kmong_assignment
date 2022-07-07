package com.example.kmong_assignment.dto.order;

import com.example.kmong_assignment.domain.Product;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Integer price;

    public static ProductDto entityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setPrice(product.getPrice());
        productDto.setName(product.getName());

        return productDto;
    }
}
