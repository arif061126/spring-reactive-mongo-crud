package com.spring.reactive.mongo.controller;

import com.spring.reactive.mongo.dto.ProductDto;
import com.spring.reactive.mongo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public Flux<ProductDto> getAllProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id){
        return productService.getProductById(id);
    }

    @GetMapping("/product-range")
    public Flux<ProductDto> getProductBetweenPriceRange(
            @RequestParam("min") Double minRange,
            @RequestParam("max") Double maxRange){
        return productService.getProductBetweenPriceRange(minRange, maxRange);
    }

    @PostMapping("/save")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return productService.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDto> updateProductById(
            @RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id){
        return productService.updateProduct(productDtoMono,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProductById(@PathVariable String id){
        return productService.deleteProductById(id);
    }
}
