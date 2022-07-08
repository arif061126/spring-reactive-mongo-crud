package com.spring.reactive.mongo.service;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.spring.reactive.mongo.dto.ProductDto;
import com.spring.reactive.mongo.repository.ProductRepository;
import com.spring.reactive.mongo.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> getProducts(){
        return repository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id){
        return repository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductBetweenPriceRange(Double minPrice, Double maxPrice){
        return repository.findByPriceBetween(Range.closed(minPrice,maxPrice));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(repository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id){
        return repository.findById(id) //getting record from db
                .flatMap(p->productDtoMono.map(AppUtils::dtoToEntity)) //request object(productDtoMono)->convert to entity
                .doOnNext(e->e.setId(id)) //as i just updating the field, keep the id as it is
                .flatMap(repository::save) //as i mapped that value i save it
                .map(AppUtils::entityToDto); //return entity to dto.
    }

    public Mono<Void> deleteProductById(String id){
        return repository.deleteById(id);
    }

}
