package com.spring.reactive.mongo.util;

import com.spring.reactive.mongo.dto.ProductDto;
import com.spring.reactive.mongo.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static ProductDto entityToDto(Product product){
        ProductDto productDto = new ProductDto();

        //when the properties of source class is the same to the properties of destination
        BeanUtils.copyProperties(product, productDto);

        return productDto;

    }

    public static Product dtoToEntity(ProductDto productDto){
        Product product = new Product();

        //when the properties of source class is the same to the properties of destination
        BeanUtils.copyProperties(productDto, product);

        return product;

    }
}
